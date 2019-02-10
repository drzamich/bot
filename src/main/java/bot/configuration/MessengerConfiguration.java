package bot.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="messenger4j")
@Data
public class MessengerConfiguration {

    private String appSecret;
    private String pageAccessToken;
    private String verifyToken;
}