package bot.externalservice.siptw;

import bot.externalservice.siptw.data.Departure;
import bot.externalservice.siptw.data.Platform;
import bot.externalservice.siptw.data.PlatformRaw;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@ToString
public class SipServiceResponse {

    private List<Departure> departures;
    private List<PlatformRaw> platforms;

    public SipServiceResponse(Departure[] departures) {
        this.departures = Arrays.asList(departures);
    }

    public SipServiceResponse(PlatformRaw[] platforms){
        this.platforms = Arrays.asList(platforms);
    }
}
