package bot.externalservice.ztm.impl;

import bot.externalservice.ztm.ZtmScraper;
import bot.externalservice.ztm.response.ZtmDeparture;
import bot.externalservice.ztm.response.ZtmPlatform;
import bot.externalservice.ztm.response.ZtmStation;
import bot.service.PlatformService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("productionZtm")
@Slf4j
public class ZtmScraperImpl implements ZtmScraper {
    private static final int JSOUP_TIMEOUT_MILLIS = 100000;
    private static final String TIME_ZONE = ZtmConstants.TIME_ZONE;

    private PlatformService platformService;


    @Autowired
    public ZtmScraperImpl(PlatformService platformService) {
        this.platformService = platformService;
    }

    public List<ZtmStation> getZtmStationList() {
        List<ZtmStation> stations = Collections.emptyList();
        try {
            Document doc = Jsoup.connect(ZtmConstants.AGGREGATE_PAGE_URL).timeout(JSOUP_TIMEOUT_MILLIS).userAgent("Mozilla").get();
            Elements stationLinks = doc.select(".timetable-stops a:contains(Warszawa)");
            stations = generateStations(stationLinks);
        } catch (IOException e) {
            log.error("Could not scrape ZTM at URL: " + ZtmConstants.AGGREGATE_PAGE_URL, e);
        }
        return stations;
    }

    private List<ZtmStation> generateStations(Elements links) {
        List<ZtmStation> stationList = new ArrayList<>();
        for (Element link : links) {
            String stationName = link.text().split(" \\(Warszawa\\)")[0];
            String url = link.attr("href");
            MultiValueMap<String, String> parameters =
                    UriComponentsBuilder.fromUriString(url).build().getQueryParams();
            String id = parameters.getFirst("wtp_st");
            List<String> excludedIDs = platformService.getExcludedStationIDs();
            if (!excludedIDs.contains(id)) {
                List<ZtmPlatform> platforms = generatePlatforms(url, stationName);
                ZtmStation station = new ZtmStation(id, stationName, url, platforms);
                stationList.add(station);
                System.out.println(station);
            }
        }
        return stationList;
    }

    private List<ZtmPlatform> generatePlatforms(String stationUrl, String stationName) {
        List<ZtmPlatform> ztmPlatforms = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(stationUrl).timeout(JSOUP_TIMEOUT_MILLIS).userAgent("Mozilla").get();
            Elements platformElements = doc.select(".timetable-stop-point");
            for(Element el: platformElements) {
                String[] numberWrapper = el.select(".timetable-stop-point-title-name").text().trim().split(" ");
                String number = numberWrapper[numberWrapper.length - 1];
                String platformIdentifier = stationName + " " + number;
                if (platformService.getExcludedPlatformNames().contains(platformIdentifier)) {
                    continue;
                }
                String direction = el.select(".timetable-stop-point-title-destination span").text();
                List<String> lines = el.select(".timetable-stop-point-block-items a").stream().map(Element::text).collect(Collectors.toList());
                String url = el.select(".timetable-stop-point-title a").attr("href");
                List<ZtmDeparture> departures = generateDepartures(url, stationName, number);
                ztmPlatforms.add(new ZtmPlatform(number, direction, lines, url, departures));
            }

        } catch (IOException e) {
            log.error("Could not scrape ZTM details of station {} at url {}", stationName, stationUrl, e);
        }
        return ztmPlatforms;
    }

    private List<ZtmDeparture> generateDepartures(String platformUrl, String stationName, String platformNumber) {
        List<ZtmDeparture> ztmDepartures = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(platformUrl).timeout(JSOUP_TIMEOUT_MILLIS).userAgent("Mozilla").get();
            Elements departureElements = doc.select(".timetable-departures-entry");
            int maxHour = 0;

            for (Element el: departureElements) {
                String line = el.select(".timetable-button-tile").text();
                String direction = el.select(".timetable-departures-entry-direction span:nth-child(2)").text();
                LocalDateTime time = generateTime(el.select(".timetable-departures-entry-hour").text(), maxHour);
                int realHour = time.getHour();
                if(realHour > maxHour) {
                    maxHour = realHour;
                }
                ztmDepartures.add(new ZtmDeparture(time, line, direction));
            }
        } catch (IOException e) {
            log.error("Could not scrape ZTM departures for platform {} of station {} at url {}", platformNumber, stationName, platformUrl);
        }
        return ztmDepartures;
    }

    private LocalDateTime generateTime(String time, int maxHour) {
        int hour = Integer.valueOf(time.split(":")[0]);
        int minute = Integer.valueOf(time.split(":")[1]);
        int plusDays = hour < maxHour ? 1 : 0;
        return LocalDateTime.now(ZoneId.of(TIME_ZONE)).withHour(hour).withMinute(minute).withSecond(0).withNano(0).plusDays(plusDays);
    }
}
