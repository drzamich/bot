package bot.externalservice.apium.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "apium")
@Data
@Validated
public class ApiUmConfiguration {

    @NotNull
    private String key;

}