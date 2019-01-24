package bot;

import bot.processor.DataManager;
import bot.processor.QueryProcessor;
import bot.schema.Station;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {

        int devMode = 1;

        if (devMode == 1) {
//            DataManager dataManager = new DataManager();
//            List<Station> finalList = dataManager.getFinalList();

        String msg = "centrum 10";
        QueryProcessor queryProcessor = new QueryProcessor();
        queryProcessor.processQuery(msg);
        System.out.println(queryProcessor.getResponse().getInfo());

        } else {
            SpringApplication.run(Application.class, args);
        }
    }


}

