package bot;

import bot.externalservice.apium.StationService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(Application.class, args);
        //DataScraper dataScraper = new DataScraper();
        StationService stationService = new StationService();
        stationService.processMsg("borowiecka");
    }
}

