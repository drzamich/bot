package bot.data;

import bot.data.Departure;
import bot.data.Platform;
import bot.data.Station;
import lombok.Data;

import java.util.ArrayList;
import java.util.Optional;

@Data
public class InformationSchema {
    private Station station;
    private Platform platform;
    private Optional<ArrayList<Departure>> departuresOpt;

    private String stationName;
    private String platformMainDir;


    public InformationSchema(Station station, Platform platform, Optional<ArrayList<Departure>> departuresOpt) {
        this.station = station;
        this.platform = platform;
        this.departuresOpt = departuresOpt;
        this.platformMainDir = platform.getMainDir();
        this.stationName = station.getStationName();
    }

    public String getInfo(){
        String res = "";
        res = res + "Station "+ this.stationName+":\n"+
                "Departures from platform in direction of "+this.platformMainDir;
        if(departuresOpt.isPresent()) {
            ArrayList<Departure> departures = departuresOpt.get();
            for (Departure departure : departures) {
                res = res + "\n" + departure.getLine() + " | " + departure.getDestination() + " | " + departure.getTimeMinutes();
            }
        }
        else {
            res = res + "\n Could not fetch departure times. Please try again.";
        }
        return res;
    }
}
