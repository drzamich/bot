package bot.externalservice.siptw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
public class SipTwPlatformDto implements Serializable {

    @JsonProperty("StopId")
    private int id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Direction")
    private String direction;

    @JsonProperty("GpsX")
    private float latitude;

    @JsonProperty("GpxY")
    private float longitude;

}
