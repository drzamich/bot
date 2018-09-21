package bot;


import bot.processor.QueryProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.crypto.Data;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(Application.class, args);

        String msg = "centrum";
        QueryProcessor queryProcessor = new QueryProcessor();
        queryProcessor.processQuery(msg);
        //DataCollector dataCollector = new DataCollector();
    }
}

