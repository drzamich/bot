package bot;


import bot.processor.DataManager;
import bot.processor.QueryProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(Application.class, args);


        new DataManager();
        String msg = "centrum 10";
        QueryProcessor queryProcessor = new QueryProcessor();
        queryProcessor.processQuery(msg);
        System.out.println(queryProcessor.getResponse().getInfo());

    }


}

