package bot;

import bot.processor.QueryProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        int devMode = 1;

        if (devMode == 1) {
            String msg = "zab";
            QueryProcessor queryProcessor = new QueryProcessor();
            queryProcessor.processQuery(msg);
            System.out.println(queryProcessor.getResponse().getInfo());
        } else {
            SpringApplication.run(Application.class, args);
        }
    }
}

