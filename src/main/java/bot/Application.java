package bot;


import bot.processor.DataManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {

//        SpringApplication.run(Application.class, args);
        DataManager dataManager = new DataManager();
        dataManager.prepareData();

//        String msg = "centrum 10";
//        QueryProcessor queryProcessor = new QueryProcessor();
//        queryProcessor.processQuery(msg);
//        System.out.println(queryProcessor.getResponse().getInfo());

    }


}

