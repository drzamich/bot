package bot.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "messenger4j")
@Data
@Validated
public class MessengerConfiguration {

    @NotNull
    private String appSecret;

    @NotNull
    private String pageAccessToken;

    @NotNull
    private String verifyToken;
}