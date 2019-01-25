package bot.schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class Response {
    private List<Station> stations;
    private Optional<Platform> platform;
    private Optional<List<Departure>> departures;
    private List<String> messages = new ArrayList<>();
    private String info;
    private String responseType;


    //How many (max) departures will be displayed in the response (
    private int maxDepartures = 7;

    public Response(List<Station> stations, Optional<Platform> platform, Optional<List<Departure>> departures,
                    String responseType) {
        this.stations = stations;
        this.platform = platform;
        this.departures = departures;
        this.responseType = responseType;
        prepareMsg();
        prepareInfo();
    }

    public void prepareMsg() {
        if (this.stations.size() < 1) {
            messages.add("Wrong station.");
            return;
        }
        else if (this.stations.size() > 1) {
            messages.add("Multiple matching stations. Select the proper one.");
            this.createButtonsMsg(this.stations);
            return;
        }

        messages.add("Departures for: " + stations.get(0).getMainName());

        if (!platform.isPresent()) {
            messages.add("Choose platform:");
            this.createButtonsMsg(this.stations.get(0));
            return;
        }

        Platform pl = platform.get();
        messages.add("Leaving from platform " + pl.getNumber() + ". Direction: " + pl.getDirections().get(0)
                + System.getProperty("line.separator") + responseType);
        this.createDepartureMsg(departures);

    }


    private void createButtonsMsg(Station s) {
        List<Button> buttons = prepareButtons(s);
        for (Button button : buttons) {
            messages.add(button.toString());
        }
    }

    private void createButtonsMsg(List<Station> stations) {
        List<Button> buttons = prepareButtons(stations);
        for (Button button : buttons) {
            messages.add(button.toString());
        }
    }

    private void createDepartureMsg(Optional<List<Departure>> deps) {
        if (deps.isPresent()) {

            if (deps.get().size() < this.maxDepartures) {
                this.maxDepartures = deps.get().size() - 1;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i <= this.maxDepartures; i++) {
                Departure departure = deps.get().get(i);
                sb.append(departure.getLine() + " | " + departure.getDirection() + " | " + departure.getTime());
                if (i <= this.maxDepartures - 1) {
                    sb.append(System.getProperty("line.separator"));
                }
            }
            messages.add(sb.toString());
        } else {
            messages.add("No departures from this platform in the nearest future.");
        }
    }


    private List<Button> prepareButtons(Station s) {
//            List<Platform> platforms = station.getPlatforms();
//            List<Button> res = new ArrayList<>();
//            for (Platform platform : platforms) {
//                String textVis = "Platform: " + platform.getNumber() + ". Direction: " + platform.getDirections().get(0);
//                String textHid = station.getMainName() + " " + platform.getNumber();
//                res.add(new Button(textVis, textHid));
//            }
//            return res;

        return s.getPlatforms()
                .stream()
                .map(p -> new Button("Platform: " + p.getNumber() + ". Direction: " + p.getMainDirection(),
                        s.getMainName() + " " + p.getNumber()))
                .collect(Collectors.toList());
    }

    private List<Button> prepareButtons(List<Station> stations) {
        return stations
                .stream()
                .map(s -> new Button(s.getMainName(),s.getMainName()))
                .collect(Collectors.toList());
    }

    private void prepareInfo() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messages.size(); i++) {
            sb.append(messages.get(i));
            if (i < messages.size() - 1) {
                sb.append(System.getProperty("line.separator"));
            }
        }
        info = sb.toString();
    }

}