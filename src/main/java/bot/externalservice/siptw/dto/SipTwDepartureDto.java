package bot.externalservice.siptw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SipTwDepartureDto {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Timestamp")
    private String timestamp;

    @JsonProperty("Line")
    private String line;

    @JsonProperty("SideNumber")
    private String sideNumber;

    @JsonProperty("StopId")
    private String stopId;

    @JsonProperty("Destination")
    private String destination;

    @JsonProperty("Arrival")
    private String arrival;

    @JsonProperty("ArrivalAcurate")
    private String arrivalAcurate;

    @JsonProperty("FromSchedule")
    private boolean fromSchedule;

    @JsonProperty("LowFloor")
    private boolean lowFloor;

    @JsonProperty("OnStop")
    private boolean onStop;

    @JsonProperty("Disruption")
    private boolean disruption;
}
