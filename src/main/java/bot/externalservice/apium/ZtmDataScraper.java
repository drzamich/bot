package bot.externalservice.apium;

import bot.schema.Platform;
import bot.schema.Station;
import bot.externalservice.general.NameProcessor;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@Service
public class ZtmDataScraper extends Properties {
    private List<Station> stationList = new ArrayList<>();
    private final List<String> EXCLUDED_IDS = Arrays.asList("2306");
    protected Map<String,Station> stationsMap = new HashMap<>();
    private final String BASE_URL = "http://www.ztm.waw.pl/";


    public ZtmDataScraper(){
    }

    public List<Station> getZtmStationList(){
        try {
            this.fillStationList();
            this.fillPlatformInformation();;
        }
        catch (Exception e){
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
                station.setPlatforms(platforms);
                System.out.println(station);
        }
    }

    private List<Platform> generatePlatformList(Elements links, Elements directions, Elements linesContainers, Station station){
        List<Platform> res = new ArrayList<>();
        for (int i = 0; i < links.size(); i++) {
            String[] platformNumberWrapper = links.get(i).text().split(" ");
            String platformNumber = platformNumberWrapper[platformNumberWrapper.length-1];

            List<String> directs= new ArrayList<>();
            directs.add(directions.get(i).text());

            Elements linesLinks = linesContainers.get(i).select("a");
            List<String> lines = new ArrayList<>();
            for (Element linesLink : linesLinks) {
                lines.add(linesLink.text());
            }

            String platformName = station.getMainName()+" "+platformNumber;

            if(!BLOCKED_STOPS.contains(platformName)) {
                res.add(new Platform(platformNumber, directs, lines));
            }
        }
        return res;
    }

    private void fillStationList() throws Exception {
        String url="http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1";
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a:contains(Warszawa)");
        this.generateStations(links);
    }

    private void generateStations(Elements links){
        for (Element link : links) {
            String stationName = link.text();
            String url = BASE_URL+link.attr("href");

            int pos = url.indexOf("&a=");
            String id = url.substring(pos + 3);

            stationName = stationName.replaceAll("\\(Warszawa\\)", "").trim();

            if (!EXCLUDED_IDS.contains(id)) {
                Station station = new Station(id,stationName, url);
                this.stationList.add(station);
            }
        }
        //this.checkForNameRepetittions();
    }

//    private Station makeStation(String stationName, String id, String url) {
//        Station station = new Station(id, stationName.trim(), url);
//
//       // NameProcessor nameProcessor = new NameProcessor(stationName);
//       // List<String> acceptedNames = nameProcessor.getAcceptedNames();
//       // station.setAcceptedNames(acceptedNames);
//
//        return station;
//    }

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



