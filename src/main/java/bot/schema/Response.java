package bot.schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Response {
    private Optional<Station> station;
    private Optional<Platform> platform;
    private Optional<List<Departure>> departures;
    private List<String> messages = new ArrayList<>();
    private String info;
    private int maxDepartures = 7;


    public Response(Optional<Station> station, Optional<Platform> platform, Optional<List<Departure>> departures) {
        this.station = station;
        this.platform = platform;
        this.departures = departures;
        prepareMsg();
        prepareInfo();
    }

    public void prepareMsg() {
        if (!station.isPresent()) {
            messages.add("Wrong station.");
            return;
        }

        messages.add("Departures for: " + station.get().getMainName());

        if (!platform.isPresent()) {
            messages.add("Choose platform:");
            this.createButtonsMsg(this.station.get());
            return;
        }

        Platform pl = platform.get();
        messages.add("Leaving from platform " + pl.getNumber() + ". Direction: " + pl.getDirection());
        this.createDepartureMsg(departures);

    }

    private void createButtonsMsg(Station station){
        List<Button> buttons = prepareButtons(station);
        for (Button button : buttons) {
            messages.add(button.toString());
        }
    }

    private void createDepartureMsg(Optional<List<Departure>> deps){
        if (deps.isPresent()) {

            if(deps.get().size()<this.maxDepartures){
                this.maxDepartures = deps.get().size()-1;
            }

            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i <= this.maxDepartures; i++) {
                Departure departure = deps.get().get(i);
                sb.append(departure.getLine() + " | " + departure.getDirection() + " | " + departure.getTime());
                if(i <= this.maxDepartures-1){
                    sb.append(System.getProperty("line.separator"));
                }
            }
            messages.add(sb.toString());
        } else {
            messages.add("Not able to present departures for this platform.");
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

    private void prepareInfo(){
        StringBuilder sb = new StringBuilder();
        for(int i =0; i<messages.size();i++){
            sb.append(messages.get(i));
            if(i<messages.size()-1) {
                sb.append(System.getProperty("line.separator"));
            }
        }
        info = sb.toString();
    }

}