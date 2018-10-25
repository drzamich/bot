package bot.externalservice.apium;

import bot.schema.Platform;
import bot.schema.Station;
import bot.processor.Utilities;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

@Service
public class ApiUmDataCollector extends Properties {
    //    private List<String> fetchedStations = Arrays.asList("Muranów","Centrum","Muranowska","Kierbedzia","Kijowska",
//                        "Hipodrom","GUS","Metro Świętokrzyska","Metro Politechnika","Dw.Centralny");
    private List<String> fetchedStations = Arrays.asList("Muranów", "Centrum");
    //    private List<String> fetchedStations = Arrays.asList();
    private List<Station> stationList;
    private List<Station> stationsWithTrams = new ArrayList<>();

    public ApiUmDataCollector() {
        //getStationList();
//        countStations();
//        countStationsWithTrams();
//        generateTimetbles2();
        //System.out.println("Data collected.");
    }

    public void countStations() {
        System.out.println("All stations");
        countPlatforms(this.stationList);
        System.out.println("With trams");
        countStationsWithTrams();
        countTramQueries();
    }

    public List<Station> getList() {
        return this.stationList;
    }

    public List<Station> getStationList() {
        DataScraper dataScraper = new DataScraper();
        return dataScraper.getStationList();

    }

    public void countPlatforms(List<Station> stationList) {
        int platforms = 0;
        int platformsLines = 0;
        for (Station station : stationList) {
            platforms = platforms + station.getPlatforms().size();
            for (Platform platform : station.getPlatforms()) {
                platformsLines = platformsLines + platform.getLines().size();
            }
        }
        System.out.println("Number of stations: " + stationList.size());
        System.out.println("Number of platforms: " + platforms);
        System.out.println("Number of lines in platforms: " + platformsLines);
    }

    public void countStationsWithTrams() {
//        stationList.stream()
//                .filter(
//                        s -> s.getPlatforms().stream()
//                                            .filter(p -> p.getLines().stream()
//
//                )
//                .forEach(System.out::println);
        boolean hasTrams;
        for (Station station : this.stationList) {
            hasTrams = false;
            for (Platform platform : station.getPlatforms()) {
                for (String s : platform.getLines()) {
                    if (Utilities.isNumeric(s)) {
                        int lineNumber = Integer.valueOf(s);
                        if (lineNumber >= 1 && lineNumber <= 35) {
                            hasTrams = true;
                        }
                    }
                }
            }
            if (hasTrams) {
                stationsWithTrams.add(station);
            }
        }
        this.countPlatforms(stationsWithTrams);

    }

    public void countTramQueries() {
        int count = 0;

        for (Station station : this.stationList) {
            for (Platform platform : station.getPlatforms()) {
                for (String s : platform.getLines()) {
                    if (Utilities.isNumeric(s)) {
                        int lineNumber = Integer.valueOf(s);
                        if (lineNumber >= 1 && lineNumber <= 35) {
                            count = count + 1;
                        }
                    }
                }
            }
        }
        System.out.println("Queries for trams: " + count);

    }

    public void generateTimetbles2() {
        final int parallelism = 100;

        ForkJoinPool forkJoinPool = null;

        try {
            forkJoinPool = new ForkJoinPool(parallelism);
            forkJoinPool.submit(() ->

                    stationList.parallelStream()
                            //.filter(s -> fetchedStations.contains(s.getMainName()))
                            .forEach(ApiUmDataCollector::getTimetable)

            ).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (forkJoinPool != null) {
                forkJoinPool.shutdown();
            }
        }


    }

    public void generateTimetables() {
        for (Station station : stationList) {
            Station searchedStation = null;

            if (fetchedStations.isEmpty()) {
                searchedStation = station;
            } else if (!fetchedStations.isEmpty() && fetchedStations.contains(station.getMainName())) {
                searchedStation = station;
            }

            if (searchedStation != null) {
                new ApiUmTimetableCollector(searchedStation);
                System.out.println("Fetched timetable for " + station.getMainName());
            }
        }
        System.out.println("Timetables generated.");
    }

    public static void getTimetable(Station station) {
        new ApiUmTimetableCollector(station);
        System.out.println("Fetched timetable for " + station.getMainName());
    }
}
