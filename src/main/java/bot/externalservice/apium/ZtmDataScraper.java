package bot.externalservice.apium;

import bot.Settings;
import bot.schema.Platform;
import bot.schema.Station;
import bot.utils.FileHelper;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Service
public class ZtmDataScraper {
    private List<Station> stationList = new ArrayList<>();
    private Map<String, Station> stationsMap = new HashMap<>();
    private final String BASE_URL = "http://www.ztm.waw.pl/";

    private List<String> excludedPlatforms;
    private List<String> excludedIDs;

    public List<Station> getZtmStationList() {
        try {
            this.excludedPlatforms = FileHelper.readFile(Settings.MAIN_DATA_PATH + "excludedPlatforms");
            this.excludedIDs = FileHelper.readFile(Settings.MAIN_DATA_PATH + "excludedIDs");
            fillStationList();
            fillPlatformInformation();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Not able to get station list");
        }
        return this.stationList;
    }


    private void fillPlatformInformation() throws Exception {
        for (Station station : this.stationList) {
            String url = station.getUrlToPlatforms();
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select(".PrzystanekKierunek p:contains(przystanek)>a>strong");
            Elements directions = doc.select(".PrzystanekKierunek p:contains(przystanek)>strong");
            Elements linesContainers = doc.select(".PrzystanekLineList");

            List<Platform> platforms = generatePlatformList(links, directions, linesContainers, station);
            if(!platforms.isEmpty()) {
                station.setPlatforms(platforms);
                System.out.println(station);
            }
        }
    }

    private List<Platform> generatePlatformList(Elements links, Elements directions, Elements linesContainers, Station station) {
        List<Platform> res = new ArrayList<>();
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

            if (!this.excludedPlatforms.contains(platformName) && !lines.isEmpty()) {
                res.add(new Platform(platformNumber, directs.get(0), directs, lines));
            }
        }
        return res;
    }

    private void fillStationList() throws Exception {
        String url = "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1";
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a:contains(Warszawa)");
        this.generateStations(links);
    }

    private void generateStations(Elements links) {
        for (Element link : links) {
            String stationName = link.text();
            String url = BASE_URL + link.attr("href");

            int pos = url.indexOf("&a=");
            String id = url.substring(pos + 3);

            stationName = stationName.replaceAll("\\(Warszawa\\)", "").trim();

            if (!this.excludedIDs.contains(id)) {
                Station station = new Station(id, stationName, url);
                this.stationList.add(station);
            }
        }
        //this.checkForNameRepetittions();
    }


    private void checkForNameRepetittions() {
        List<String> names = new ArrayList<>();
        boolean noRepetitions = true;
        for (Station station : this.stationList) {
            List<String> namess = station.getAcceptedNames();
            for (String name : namess) {
                if (!names.contains(name)) {
                    names.add(name);
                } else {
                    System.out.println("Error. Name repetition: " + name);
                    System.out.println(station);
                    noRepetitions = false;
                }
            }
        }
        if (noRepetitions) {
            System.out.println("Checking for repetitions finished successfully");
        }
    }

}