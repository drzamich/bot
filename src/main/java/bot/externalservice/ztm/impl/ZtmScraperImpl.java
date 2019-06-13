package bot.externalservice.ztm.impl;

import bot.externalservice.ztm.ZtmScraper;
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
import java.util.*;
import java.util.stream.Collectors;

@Service("productionZtm")
@Slf4j
public class ZtmScraperImpl implements ZtmScraper {

    private static final String BASE_URL = "http://www.ztm.waw.pl/";
    private static final String AGGREGATE_PAGE_PATH = "rozklad_nowy.php?c=183&l=1";
    private static final int JSOUP_TIMEOUT_MILLIS = 3000;

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
                createStation(stationList, stationName, url, id);
            }
        }
        return stationList;
    }

    private void createStation(List<ZtmStation> stationList, String stationName, String url, String id) {
        ZtmStation station = new ZtmStation(id, stationName, url);
        stationList.add(station);
        String urlToPlatforms = station.getUrlToPlatforms();
        try {
            Document doc = Jsoup.parse(new URL(urlToPlatforms), JSOUP_TIMEOUT_MILLIS);
            Elements linksToPlatforms = doc.select(".PrzystanekKierunek p:contains(przystanek)>a>strong");
            Elements directions = doc.select(".PrzystanekKierunek p:contains(przystanek)>strong");
            Elements linesContainers = doc.select(".PrzystanekLineList");
            List<ZtmPlatform> platforms = generatePlatformList(linksToPlatforms, directions, linesContainers, station);
            if (!platforms.isEmpty()) {
                station.setPlatforms(platforms);
                log.debug("Station {} fetched from url: {}" + station.toString(), urlToPlatforms);
            }
        } catch (IOException e) {
            log.error("Could not scrape ZTM details of station {} at url {}", station.toString(), urlToPlatforms, e);
        }
    }

    private List<ZtmPlatform> generatePlatformList(Elements links, Elements directions, Elements linesContainers, ZtmStation station) {
        List<ZtmPlatform> res = new ArrayList<>();
        for (int i = 0; i < links.size(); i++) {
            String[] platformNumberWrapper = links.get(i).text().split(" ");
            String platformNumber = platformNumberWrapper[platformNumberWrapper.length - 1];
            List<String> directs = new ArrayList<>();
            directs.add(directions.get(i).text());
            Elements linesLinks = linesContainers.get(i).select("a");
            List<String> lines = linesLinks
                    .stream()
                    .map(Element::text)
                    .collect(Collectors.toList());
            String platformName = station.getMainName() + " " + platformNumber;
            List<String> excludedPlatforms = platformService.getExcludedPlatformNames();
            if (!excludedPlatforms.contains(platformName) && !lines.isEmpty()) {
                res.add(new ZtmPlatform(platformNumber, directs.get(0), directs, lines));
            }
        }
        return res;
    }
}