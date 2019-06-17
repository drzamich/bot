package bot.externalservice.siptw.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "siptw")
@Data
@Validated
public class SipTwConfiguration  {

    @NotNull
    private String key;
}
