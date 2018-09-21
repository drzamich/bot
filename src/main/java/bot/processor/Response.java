package bot.processor;

import bot.data.Departure;
import bot.data.PlatformDepartureInfo;
import bot.externalservice.apium.data.Platform;
import bot.externalservice.apium.data.Station;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Response {
    private Station station;
    private List<Platform> platforms;
    private List<PlatformDepartureInfo> platformDepartureInfos;
    private List<String> messages = new ArrayList<>();
    private int maxDepartures = 7;


    public Response(Station station, List<Platform> platforms, List<PlatformDepartureInfo> platformDepartureInfo) {
        this.station = station;
        this.platforms = platforms;
        this.platformDepartureInfos = platformDepartureInfo;
    }

    public void prepareMsg() {
        if (station == null) {
            messages.add("Wrong station.");
            return;
        }

        messages.add("Departures for: " + station.getMainName());

        if (platforms.isEmpty()) {
            messages.add("Choose platform:");
            List<Button> buttons = prepareButtons(station);
            for (Button button : buttons) {
                messages.add(button.toString());
            }
            return;
        }

        for (PlatformDepartureInfo plDep : platformDepartureInfos) {
            Platform pl = plDep.getPlatform();
            messages.add("Leaving from platform " + pl.getNumber() + ". Direction: " + pl.getDirection());
            if (!plDep.getDepartures().isEmpty()) {
                String msg = "";
                for (int i = 0; i <= this.maxDepartures; i++) {
                    Departure departure = plDep.getDepartures().get(i);
                    msg = msg + departure.getLine() + " | " + departure.getDirection() + " | " + departure.getTime() + "\n";
                }
                messages.add(msg);
            } else {
                messages.add("Not able to present departures for this platform.");
            }
        }
    }


    private List<Button> prepareButtons(Station station) {
        List<Platform> platforms = station.getPlatforms();
        List<Button> res = new ArrayList<>();
        for (Platform platform : platforms) {
            String textVis = "Platform: " + platform.getNumber() + ". Direction: " + platform.getDirection();
            String textHid = station.getMainName() + " " + platform.getNumber();
            res.add(new Button(textVis, textHid));
        }
        return res;
    }

}