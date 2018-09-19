package bot.externalservice.apium;

import bot.externalservice.apium.data.Platform;
import bot.externalservice.apium.data.Station;
import bot.externalservice.general.NameProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;

public class DataScraper {
    private List<Station> stations = new ArrayList<>();
    private final List<String> EXCLUDED_IDS = Arrays.asList("2306");
    private final String BASE_URL = "";

    public DataScraper() throws Exception {
        this.fillStationList();
        this.fillPlatformInformation();
    }


    private void fillPlatformInformation() throws Exception {
        for (Station station : this.stations) {
            String url = station.getUrlToPlatforms();

            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select(".PrzystanekKierunek p:contains(przystanek)>a>strong");
            Elements directions = doc.select(".PrzystanekKierunek p:contains(przystanek)>strong");
            Elements linesContainers = doc.select(".PrzystanekLineList");

            List<Platform> platforms = generatePlatformList(links,directions,linesContainers);
            station.setPlatforms(platforms);

            System.out.println(station);
        }
    }

    private List<Platform> generatePlatformList(Elements links, Elements directions, Elements linesContainers){
        List<Platform> res = new ArrayList<>();
        for (int i = 0; i < links.size(); i++) {
            String[] platformNumberWrapper = links.get(i).text().split(" ");
            String platformNumber = platformNumberWrapper[platformNumberWrapper.length-1];

            String direction = directions.get(i).text();

            Elements linesLinks = linesContainers.get(i).select("a");
            List<String> lines = new ArrayList<>();
            for (Element linesLink : linesLinks) {
                lines.add(linesLink.text());
            }

            res.add(new Platform(platformNumber,direction,lines));
        }
        return res;
    }

    private void fillStationList() throws Exception {
        File input = new File("E:\\java\\bot\\src\\main\\resources\\scraping\\ztmWebsite\\station_list.html");
        Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
        Elements links = doc.select("a:contains(Warszawa)");
        this.generateStations(links);
    }

    private void generateStations(Elements links){
        for (Element link : links) {
            String stationName = link.text();
            String url = link.attr("href");

            int pos = url.indexOf("&a=");
            String id = url.substring(pos + 3);

            stationName = stationName.replaceAll("\\(Warszawa\\)", "");

            if (!EXCLUDED_IDS.contains(id)) {
                Station station = this.makeStation(stationName, id, url);
                this.stations.add(station);
            }
        }
        this.checkForNameRepetittions();
    }

    private Station makeStation(String stationName, String id, String url) {
        Station station = new Station(id, stationName.trim(), url);

        NameProcessor nameProcessor = new NameProcessor(stationName);
        List<String> acceptedNames = nameProcessor.getAcceptedNames();
        station.setAcceptedNames(acceptedNames);

        return station;
    }

    private void checkForNameRepetittions() {
        List<String> names = new ArrayList<>();
        boolean noRepetitions = true;
        for (Station station : this.stations) {
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



