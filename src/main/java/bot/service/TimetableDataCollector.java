package bot.service;

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
public class TimetableDataCollector {

    private List<String> fetchedStations;

    private List<Station> stationList;

    private TimetableGenerator timetableGenerator;
    private DataManager dataManager;

    @Autowired
    public TimetableDataCollector(TimetableGenerator timetableGenerator, DataManager dataManager) {
        this.timetableGenerator = timetableGenerator;
        this.dataManager = dataManager;
    }

    public void doWork() {
        prepareData();
        generateTimetables();
        clearOldData();
    }

    private void clearOldData() {

//        timetableRepository.deleteAll();

        File folder = new File(Settings.PATH_SAVED_TIMETABLES);
        File[] files = folder.listFiles();



        Arrays.stream(files)
                .filter(f-> !f.getName().contains(StringHelper.getTime(Settings.DATE_PATTERN)))
                .forEach(File::delete);
    }

    private void prepareData() {
//        timetableRepository.getStations();

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
        timetableGenerator.generateTimetablesForStation(station);
        log.info("Fetched timetable for " + station.getMainName());
    }
}
