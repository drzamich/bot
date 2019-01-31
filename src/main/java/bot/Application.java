package bot;

//import bot.processor.Query;
//import bot.processor.QueryProcessor;
import com.github.messenger4j.Messenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

    @Bean
    public Messenger messenger(@Value("${messenger4j.pageAccessToken}") String pageAccessToken,
                               @Value("${messenger4j.appSecret}") final String appSecret,
                               @Value("${messenger4j.verifyToken}") final String verifyToken) {
        return Messenger.create(pageAccessToken, appSecret, verifyToken);
    }

    public static void main(String[] args) {

        int devMode = 0;

        if (devMode == 1) {
            String msg = "muranowska centrum";
            System.out.println(System.getProperty("user.home"));
//            Query q = new Query(msg);
////            queryProcessor.processQuery(msg);
//            System.out.println(q.getFullResponse().getConsoleInfo());
//            System.out.println(queryProcessor.getFullResponse().getResponseJSONString());
        } else {
            SpringApplication.run(Application.class, args);
        }
    }
}

