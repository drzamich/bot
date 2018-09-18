package bot.externalservice.siptw;

import bot.externalservice.siptw.data.Departure;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@Getter
@ToString
public class SipServiceResponse {

    private List<Departure> departures;

    public SipServiceResponse(Departure[] departures) {
        this.departures = Arrays.asList(departures);
    }
}
