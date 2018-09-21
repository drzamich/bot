package bot.externalservice.apium;

import bot.externalservice.apium.data.Station;
import bot.processor.Utilities;
import java.util.Arrays;
import java.util.List;

public class DataCollector extends DataManager {
    //    private List<String> fetchedStations = Arrays.asList("Muranów");
    private List<String> fetchedStations = Arrays.asList();
    private List<Station> stationList;

    public DataCollector() {
        getStationList();
        //generateTimetables();
        System.out.println("Data collected.");
    }

    public void getStationList() {

        if (!Utilities.objectExists(pathToStationMap)) {
            DataScraper dataScraper = new DataScraper();
        }

        this.stationList = Utilities.deserializeObject(pathToStationList);

        System.out.println("Station list generated.");
    }

    public void generateTimetables() {
        for (Station station : stationList) {
            Station searchedStation = null;

            if(fetchedStations.isEmpty()){
                searchedStation = station;
            }
            else if (!fetchedStations.isEmpty() && fetchedStations.contains(station.getMainName())) {
                searchedStation = station;
            }

            if (searchedStation != null) {
                new StationService(searchedStation);
                System.out.println("Fetched timetable for " + station.getMainName());
            }
        }
        System.out.println("Timetables generated.");
    }
}
