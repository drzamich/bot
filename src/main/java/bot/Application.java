package bot;


import bot.externalservice.general.NameProcessor;
import bot.processor.DataManager;
import bot.processor.QueryProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        new DataManager();

       // SpringApplication.run(Application.class, args);

//        String msg = "centrum 10";
//        QueryProcessor queryProcessor = new QueryProcessor();
//        queryProcessor.processQuery(msg);
//        System.out.println(queryProcessor.getResponse().getInfo());

    }


}

