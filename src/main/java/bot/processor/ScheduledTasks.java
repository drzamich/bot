package bot.processor;

import bot.service.TimetableDataCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ScheduledTasks {

    private TimetableDataCollector timetableDataCollector;

    @Autowired
    public ScheduledTasks(TimetableDataCollector timetableDataCollector) {
        this.timetableDataCollector = timetableDataCollector;
    }

    @Scheduled(cron="0 3 3 * * *")  //this will run at 3:03 AM every day
    public void performEverydayTasks(){
        log.info("Starting data collection");
        timetableDataCollector.doWork();
        log.info("Data collection finished");
    }
}
