package bot.processor;

import bot.externalservice.apium.ApiUmDataCollector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Scheduled(cron="0 3 3 * * *")  //this will run at 3:03 AM every day
    public void performEverydayTasks(){
        System.out.println("Starting data collection");
        ApiUmDataCollector a = new ApiUmDataCollector();
        a.doWork();
        System.out.println("Data collection finished");
    }
}
