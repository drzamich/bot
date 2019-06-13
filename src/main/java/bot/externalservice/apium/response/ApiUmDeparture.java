package bot.externalservice.apium.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ApiUmDeparture {

    private String line;

    private String destination;

    private String departureTime;
}
