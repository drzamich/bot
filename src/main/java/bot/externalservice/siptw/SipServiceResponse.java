package bot.externalservice.siptw;


import bot.externalservice.siptw.data.DepartureSipTw;
import bot.externalservice.siptw.data.PlatformRaw;
import bot.schema.Departure;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@ToString
public class SipServiceResponse {

    private List<DepartureSipTw> departuresFromSipTw;
    private List<Departure> departures;
    private List<PlatformRaw> platforms;


    public SipServiceResponse(DepartureSipTw[] departures) {
        this.departuresFromSipTw = Arrays.asList(departures);
        this.departures = new ArrayList<>();
        for(DepartureSipTw dep: departuresFromSipTw){
            this.departures.add(new Departure(dep.getLine(),dep.getDestination(),dep.getTimeMinutes()));
        }
    }

    public SipServiceResponse(PlatformRaw[] platforms){
        this.platforms = Arrays.asList(platforms);
    }
}
