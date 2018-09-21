package bot;

import bot.externalservice.apium.DataCollector;
import bot.externalservice.apium.StationService;
import bot.processor.QueryProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.crypto.Data;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(Application.class, args);
        //DataScraper dataScraper = new DataScraper();
//        StationService stationService = new StationService();
//        stationService.processMsg("centrum");

//        String msg = "muranow all";
//        QueryProcessor queryProcessor = new QueryProcessor();
//        queryProcessor.processQuery(msg);
        DataCollector dataCollector = new DataCollector();
    }
}

