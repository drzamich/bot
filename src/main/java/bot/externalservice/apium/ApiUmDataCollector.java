package bot.externalservice.apium;

import bot.Settings;
import bot.processor.DataManager;
import bot.schema.Station;
import bot.utils.FileHelper;
import bot.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApiUmDataCollector {

    private List<String> fetchedStations;

    private List<Station> stationList;

    private ApiUmTimetableGenerator apiUmTimetableGenerator;
    private DataManager dataManager;

    @Autowired
    public ApiUmDataCollector(ApiUmTimetableGenerator apiUmTimetableGenerator, DataManager dataManager) {
        this.apiUmTimetableGenerator = apiUmTimetableGenerator;
        this.dataManager = dataManager;
    }

    public void doWork() {
        prepareData();
        generateTimetables();
        clearOldData();
    }

    private void clearOldData() {
        File folder = new File(Settings.PATH_SAVED_TIMETABLES);
        File[] files = folder.listFiles();

        Arrays.stream(files).filter(f-> !f.getName().contains(StringHelper.getTime(Settings.DATE_PATTERN))).forEach(File::delete);
    }

    private void prepareData() {
        this.fetchedStations = FileHelper.readFile(Settings.MAIN_DATA_PATH + "fetchedStations");
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
                            .forEach(this::getTimetable)
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Cannot generate timetable", e);
        } finally {
            if (forkJoinPool != null) {
                forkJoinPool.shutdown();
            }
        }
    }
    private void getTimetable(Station station) {
        apiUmTimetableGenerator.generateTimetablesForStation(station);
        log.info("Fetched timetable for " + station.getMainName());
    }
}
