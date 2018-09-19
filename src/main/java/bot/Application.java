package bot;

import bot.externalservice.apium.DataScraper;
import bot.externalservice.apium.data.DataScraper2;
import bot.processor.Utilities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(Application.class, args);
        DataScraper dataScraper = new DataScraper();
    }
}

