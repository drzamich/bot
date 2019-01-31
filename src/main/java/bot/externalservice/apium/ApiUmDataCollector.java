package bot.externalservice.apium;

import bot.processor.DataManager;
import bot.schema.Station;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
public class ApiUmDataCollector extends Properties {
    private List<String> fetchedStations = new ArrayList<>();
    private List<Station> stationList;

    public ApiUmDataCollector() {

    }

    public void doWork() {
        prepareData();
        generateTimetables();
        clearOldData();
    }

    private void clearOldData() {
        File folder = new File(PATH_TO_OBJECTS);
        File[] files = folder.listFiles();

        Arrays.stream(files).filter(f-> !f.getName().contains(date)).forEach(File::delete);
    }

    private void prepareData() {
        readFetchedStationsFile();
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

    private void readFetchedStationsFile() {
        String pathString = MAIN_PATH + "fetchedStations";
        Path path = Paths.get(pathString);
        try {
            List<String> l = Files.readAllLines(path);
            fetchedStations = l.stream()
                                .filter(s-> !s.contains("//"))
                                .collect(Collectors.toList());
        } catch (Exception e) {

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
