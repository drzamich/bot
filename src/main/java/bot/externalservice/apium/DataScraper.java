package bot.externalservice.apium;

import bot.externalservice.apium.data.Station;
import bot.processor.Utilities;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;

public class DataScraper {
    private File input = new File("E:\\java\\bot\\src\\main\\resources\\scraping\\ztmWebsite\\station_list.html");
    private List<Station> stations = new ArrayList<>();
    private final List<String> EXCLUDED_IDS = Arrays.asList("2306");
    private final String BASE_URL = "";

    public DataScraper() throws Exception {
        Document doc = Jsoup.parse(this.input, "UTF-8", "http://example.com/");
        this.fillStationList(doc);+
    }


    private void fillPlatformInformation(){
        for(Station station : this.stations){
            String id = station.getId();
        }
    }

    private void fillPlatformInformationTest(){
        String id = 4009;
        File input = new File("E:\\java\\bot\\src\\main\\resources\\scraping\\ztmWebsite\\sample_station.html");
    }


    private void fillStationList(Document doc) {
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String stationName = link.text();
            if (stationName.contains("Warszawa")) {
                String id = link.attr("href");
                int pos = id.indexOf("&a=");
                id = id.substring(pos + 3);
                stationName = stationName.replaceAll("\\(Warszawa\\)", "");
                if (!EXCLUDED_IDS.contains(id)) {
                    Station station = this.generateStation(stationName, id);
                    this.stations.add(station);
                }
            }
        }
        this.checkForNameRepetittions();
    }

    private Station generateStation(String stationName, String id) {
        Station station = new Station(id, stationName.trim());
        List<String> acceptedNames = this.generateAcceptedNames(stationName);
        station.setAcceptedNames(acceptedNames);
        return station;
    }

    private List<String> generateAcceptedNames(String str) {
        List<String> shortcutsShort = Arrays.asList("^al", "^os", "^gen", "im ", "dw", "^zaj", "^ks", "pld", "pln", "zach", "wsch",
                "ii", "iii", "vi", "jana pawla ii", "jana pawla 2", "zajezdnia");
        List<String> shortcutsLong = Arrays.asList("aleja", "osiedle", "generala", "imienia ", "dworzec", "zajezdnia",
                "ksiedza", "poludnie", "polnoc", "zachodnia", "wschodni", "2", "3", "6", "jp2",
                "jp2", "zaj");
        List<String> repetitiveNames = Arrays.asList("metro", "pl", "al", "aleja", "plac", "dworzec");

        str = Utilities.parseInput(str);
        List<String> res = new ArrayList<>();
        res.add(str);
        String str3 = str;
        int len = shortcutsLong.size();
        for (int i = 0; i < len; i++) {
            String key = shortcutsShort.get(i);
            String val = shortcutsLong.get(i);
            String str2 = str.replaceAll(key, val);
            str3 = str3.replaceAll(key, val);
            if (!res.contains(str2)) {
                res.add(str2);
            }
            if (!res.contains(str3)) {
                res.add(str3);
            }
        }

        int len2 = res.size();
        for (int i = 0; i < len2; i++) {
            for (String key : repetitiveNames) {
                String str2 = res.get(i).replaceAll(key + " ", "").trim();
                if (!res.contains(str2) && !str.equals(str2)) {
                    res.add(str2);
                }
            }
        }

        return res;
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

    private <T> void random(List<T> a, T b){
        if(a.contains(b)){
            a.add(b);
        }
    }
}



