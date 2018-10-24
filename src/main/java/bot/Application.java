package bot;


import bot.processor.DataManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {


        DataManager dataManager = new DataManager();
        System.out.println(dataManager.getStationList());
        //SpringApplication.run(Application.class, args);
//        DataCollector dataCollector = new DataCollector();

//        String msg = "centrum 09";
//        QueryProcessor queryProcessor = new QueryProcessor();
//        queryProcessor.processQuery(msg);

    }


}

