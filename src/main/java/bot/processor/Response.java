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
    private List<PlatformDepartureInfo> platformDepartureInfos;
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
        this(station, platforms);
        this.platformDepartureInfos = platformDepartureInfo;
    }

    public void prepareMsg() {
        if (station != null) {
            String msg = "Departures for: " + station.getName();
            messages.add(msg);
        } else {
            messages.add("Wrong station.");
            return;
        }

        if (platformDepartureInfos != null) {
            for (PlatformDepartureInfo platform : platformDepartureInfos) {
                messages.add("Leaving from platform " + platform.getNumber() + ". Direction: " + platform.getDirection());
                if (platform.getDepartures().isPresent()) {
                    String msg = "";
                    for (int i = 0; i <= this.maxDepartures; i++) {
                        Departure departure = platform.getDepartures().get().get(i);
                        msg = msg + departure.getLine() + " | " + departure.getDirection() + " | " + departure.getTime() + "\n";
                    }
                    messages.add(msg);
                } else {
                    messages.add("Not able to present departures for this platform.");
                }
            }
        }
        else {
            String msg = "Choose platform:";
            messages.add(msg);
            List<Button> buttons = prepareButtons(station);
            for (Button button : buttons) {
                msg = button.toString();
                messages.add(msg);
            }
        }


    }


    private List<Button> prepareButtons(Station station) {
        List<Platform> platforms = station.getPlatforms();
        List<Button> res = new ArrayList<>();
        for (Platform platform : platforms) {
            String textVis = "Platform: " + platform.getNumber() + ". Direction: " + platform.getDirection();
            String textHid = station.getName() + " " + platform.getNumber();
            res.add(new Button(textVis, textHid));
        }
        return res;
    }

}