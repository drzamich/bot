package bot;


import bot.externalservice.apium.DataCollector;
import bot.externalservice.apium.data.Station;
import bot.processor.QueryProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(Application.class, args);

        //DataCollector dataCollector = new DataCollector();
        String msg = "muranow";
        QueryProcessor queryProcessor = new QueryProcessor();
        queryProcessor.processQuery(msg);


    }
}

