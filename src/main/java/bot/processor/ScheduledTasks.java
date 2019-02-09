package bot.processor;

import bot.externalservice.apium.ApiUmDataCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ScheduledTasks {


    private ApiUmDataCollector apiUmDataCollector;

    @Autowired
    public ScheduledTasks(ApiUmDataCollector apiUmDataCollector) {
        this.apiUmDataCollector = apiUmDataCollector;
    }

    @Scheduled(cron="0 3 3 * * *")  //this will run at 3:03 AM every day
    public void performEverydayTasks(){
        log.info("Starting data collection");
        apiUmDataCollector.doWork();
        log.info("Data collection finished");
    }
}
