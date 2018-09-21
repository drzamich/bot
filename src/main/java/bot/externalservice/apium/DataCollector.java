package bot.externalservice.apium;

import bot.externalservice.apium.data.Station;
import bot.processor.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataCollector extends DataManager {
    //    private List<String> fetchedStations = Arrays.asList("Muranów");
    private List<String> fetchedStations = Arrays.asList();
    private long timeElapsedTotal;
    private List<Station> stationList;

    public DataCollector() {
        timeElapsedTotal = 0;
        getStationList();
        generateTimetables();
        System.out.println("Data collected. Total time: " + timeElapsedTotal + " s.");
    }

    public void getStationList() {
        long startTime = System.nanoTime();

        if (!Utilities.objectExists(pathToStationMap)) {
            DataScraper dataScraper = new DataScraper();
        }

        this.stationList = Utilities.deserializeObject(pathToStationList);

        long endTime = System.nanoTime();
        long timeElapsed = (endTime - startTime) / 1000000;
        timeElapsedTotal = timeElapsedTotal + timeElapsed;
        System.out.println("Station list generated. Time elapsed: " + timeElapsedTotal + " s.");
    }

    public void generateTimetables() {
        long timetablesStart = System.nanoTime();
        for (Station station : stationList) {
            Station searchedStation = null;

            if(fetchedStations.isEmpty()){
                searchedStation = station;
            }
            else if (!fetchedStations.isEmpty() && fetchedStations.contains(station.getMainName())) {
                searchedStation = station;
            }

            if (searchedStation != null) {
                long startTime = System.nanoTime();
                new StationService(searchedStation);
                long timeElapsed = (System.nanoTime() - startTime) / 1000000;
                timeElapsedTotal = timeElapsedTotal + timeElapsed;
                System.out.println("Fetched timetable for " + station.getMainName() + ". Time for this station: " + timeElapsed + " s. Time elapsed so far: " + timeElapsedTotal + " s.");
            }

        }
        long timetablesFinish = System.nanoTime();
        long timetablesElapsed = (timetablesFinish - timetablesStart) / 1000000;
        System.out.println("Timetables generated. Time elapsed: " + timetablesElapsed + " s.");
    }
}
