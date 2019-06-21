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
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("productionZtm")
@Slf4j
public class ZtmScraperImpl implements ZtmScraper {

    private static final String BASE_URL = "https://www.ztm.waw.pl/";
    private static final String AGGREGATE_PAGE_PATH = "rozklad_nowy.php?c=183&l=1";
    private static final int JSOUP_TIMEOUT_MILLIS = 100000;
    private static final String TIME_ZONE = "CET";

    private PlatformService platformService;


    @Autowired
    public ZtmScraperImpl(PlatformService platformService) {
        this.platformService = platformService;
    }

    public List<ZtmStation> getZtmStationList() {
        String aggregatePage = BASE_URL + AGGREGATE_PAGE_PATH;
        List<ZtmStation> stations = Collections.emptyList();
        try {
            Document doc = Jsoup.parse(new URL(BASE_URL + AGGREGATE_PAGE_PATH), JSOUP_TIMEOUT_MILLIS);
            Elements stationLinks = doc.select("a:contains(Warszawa)");
            stations = populateStations(stationLinks);
        } catch (IOException e) {
            log.error("Could not scrape ZTM at URL: " + aggregatePage, e);
        }
        return stations;
    }

    private List<ZtmStation> populateStations(Elements links) {
        List<ZtmStation> stationList = new ArrayList<>();
        for (Element link : links) {
            String stationName = link.text();
            String url = BASE_URL + link.attr("href");
            MultiValueMap<String, String> parameters =
                    UriComponentsBuilder.fromUriString(url).build().getQueryParams();
            String id = parameters.getFirst("a");
            List<String> excludedIDs = platformService.getExcludedStationIDs();
            if (!excludedIDs.contains(id)) {
                List<ZtmPlatform> platforms = generatePlatforms(url, stationName);
                ZtmStation station = new ZtmStation(id, stationName, url, platforms);
                stationList.add(station);
            }
        }
        return stationList;
    }

    private List<ZtmPlatform> generatePlatforms(String stationUrl, String stationName) {
        List<ZtmPlatform> ztmPlatforms = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(new URL(stationUrl), JSOUP_TIMEOUT_MILLIS);
            Elements platformElements = doc.select(".PrzystanekKierunek");
            for(Element el: platformElements) {
                String[] numberWrapper = el.select("strong:nth-child(1)").text().split(" ");
                String number = numberWrapper[numberWrapper.length - 1];
                String platformIdentifier = stationName + " " + number;
                if (platformService.getExcludedPlatformNames().contains(platformIdentifier)) {
                    continue;
                }
                String direction = el.select("strong:nth-child(2)").text();
                List<String> lines = el.select(".PrzystanekLineList a").stream().map(Element::text).collect(Collectors.toList());
                String url = BASE_URL + el.select("a").attr("href");
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
            Document doc = Jsoup.parse(new URL(platformUrl), JSOUP_TIMEOUT_MILLIS);
            Elements times = doc.select("#PrzystanekRozklad .wwgodz");
            Elements lines = doc.select("#PrzystanekRozklad div strong");
            Elements directions = doc.select("#PrzystanekRozklad div a");
            Elements lastStops = doc.select("#PrzystanekRozklad div a span");

            int maxHour = 0;

            for(int i =0; i<times.size(); i++) {
                String line = lines.get(i).text();
                String direction = directions.get(i).text();
                String lastStopInfo = lastStops.get(i).text();
                direction = direction.replace(lastStopInfo, "").trim();
                LocalDateTime time = generateTime(times.get(i).text(), maxHour);
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