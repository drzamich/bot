package bot.externalservice.apium;

import bot.Settings;
import bot.processor.DataManager;
import bot.processor.Utilities;
import bot.schema.Station;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
public class ApiUmDataCollector {
    private List<String> fetchedStations;
    private List<Station> stationList;

    public ApiUmDataCollector() {

    }

    public void doWork() {
        prepareData();
        generateTimetables();
        clearOldData();
    }

    private void clearOldData() {
        File folder = new File(Settings.PATH_SAVED_TIMETABLES);
        File[] files = folder.listFiles();

        Arrays.stream(files).filter(f-> !f.getName().contains(Utilities.getTime(Settings.DATE_PATTERN))).forEach(File::delete);
    }

    private void prepareData() {
        this.fetchedStations = Utilities.readFile(Settings.MAIN_DATA_PATH + "fetchedStations");
        DataManager dataManager = new DataManager();
        dataManager.prepareData();
        stationList = dataManager.getIntegratedList();

        if(!fetchedStations.isEmpty()){
            stationList = stationList
                            .stream()
                            .filter(s -> fetchedStations.contains(s.getMainName()))
                            .collect(Collectors.toList());
        }
    }

    private void generateTimetables() {
        final int parallelism = 100;

        ForkJoinPool forkJoinPool = null;

        try {
            forkJoinPool = new ForkJoinPool(parallelism);
            forkJoinPool.submit(() ->

                    stationList.parallelStream()
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

    private static void getTimetable(Station station) {
        ApiUmTimetableGenerator apiUmTimetableGenerator = new ApiUmTimetableGenerator(station);
        apiUmTimetableGenerator.generateTimetables();
        System.out.println("Fetched timetable for " + station.getMainName());
    }
}
