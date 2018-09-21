package bot.externalservice.apium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataCollector {
    private List<String> fetchedStations = Arrays.asList("muranow");
    private long timeElapsedTotal;
    private List<StationService> stationList;

    public DataCollector(){
        generateStationList();
        //generateTimetables();
        System.out.println("Data collected. Total time: "+timeElapsedTotal);
    }

    public void generateStationList(){
        long startTime = System.nanoTime();
        DataScraper dataScraper = new DataScraper();
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Station list generated. Time elapsed [s]: "+ timeElapsed/1000000000);
        timeElapsedTotal += timeElapsed;
    }

    public void generateTimetables(){
        long startTime = System.nanoTime();
        for(String station: fetchedStations){
            String [] msg = {station,"all"};
            StationService stationService = new StationService(msg);
        }
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Timetables generated. Time elapsed [s]: "+ timeElapsed/1000000000);
        timeElapsedTotal += timeElapsed;
    }
}
