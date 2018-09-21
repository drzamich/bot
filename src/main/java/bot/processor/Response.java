package bot.processor;

import bot.data.Departure;
import bot.data.Platform;
import bot.data.PlatformDepartureInfo;
import bot.data.Station;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Response {
    private Station station;
    private List<Platform> platforms;
    private List<PlatformDepartureInfo> platformDepartureInfo;
    private List<String> messages = new ArrayList<>();
    private int maxDepartures = 7;

    public Response() {
        System.out.println("Wrong station");
    }

    public Response(Station station) {
        this.station = station;
    }

    public Response(Station station, List<Platform> platforms) {
        this(station);
        this.platforms = platforms;
    }

    public Response(Station station, List<Platform> platforms, List<PlatformDepartureInfo> platformDepartureInfo) {
        this(station,platforms);
        this.platformDepartureInfo = platformDepartureInfo;
    }

    public void prepareMsg(){
        if(station != null){
            messages.add("Departures for: "+station.getName());
        }
        else {
            messages.add("Wrong station.");
            return;
        }

        if(platforms != null){
            for(Platform platform: platforms){
                String platformNumber = platform.getNumber();
                messages.add("Leaving from platform "+platformNumber+". Direction: "+platform.getDirection());
                if(platformDepartureInfo != null) {
                    for (PlatformDepartureInfo platformDepartureInfo: platformDepartureInfo){
                        if(platformDepartureInfo.getNumber().equals(platformNumber)){
                            String msg = "";
                            int howMuch = 1;
                            for (Departure departure: platformDepartureInfo.getDepartures()){
                                if(howMuch<=this.maxDepartures) {
                                    msg = msg + departure.getLine() + " | " + departure.getDirection() + " | " + departure.getTime() + "\n";
                                    howMuch++;
                                }
                                else {
                                    break;
                                }
                            }
                            messages.add(msg);
                        }
                    }
                }
                else {
                    messages.add("Departures not available at all.");
                    return;
                }
            }
        }
        else {
            messages.add("Wrong platform.");
            return;
        }
    }
}
