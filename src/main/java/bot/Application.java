package bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class Application {

    private Environment environment;

    @Autowired
    public Application(Environment environment) {
        this.environment = environment;
        log.info("Running Bot with profile: " + Arrays.toString(this.environment.getActiveProfiles()));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

