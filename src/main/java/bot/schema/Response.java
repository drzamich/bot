package bot.schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class Response {
    private List<Station> stations;
    private List<Platform> platforms;
    private Optional<List<Departure>> departures;
    private List<String> messages = new ArrayList<>();
    private String info;
    private String responseType;
    private List<Button> buttonList;
    private String responseJSON;


    //How many (max) departures will be displayed in the response (
    private int maxDepartures = 7;

    public Response(List<Station> stations, List<Platform> platforms, Optional<List<Departure>> departures,
                    String responseType) {
        this.stations = stations;
        this.platforms = platforms;
        this.departures = departures;
        this.responseType = responseType;
        prepareMsg();
        prepareInfo();
        this.responseJSON = new JSONResponse(this.buttonList,this.messages).toString();
    }

    public void prepareMsg() {
        if (this.stations.size() < 1) {
            this.messages.add("Wrong station.");
            return;
        }
        else if (this.stations.size() > 1) {
            this.messages.add("Multiple matching stations. Select the proper one.");
            this.buttonList = new ButtonList(this.stations).getButtonList();
            return;
        }

        messages.add("Departures for: " + stations.get(0).getMainName());


        if (this.platforms.size() != 1) {
            this.messages.add("Choose platform:");
            this.buttonList = new ButtonList(this.stations.get(0)).getButtonList();
            return;
        }

        Platform pl = this.platforms.get(0);
        messages.add("Leaving from platform " + pl.getNumber() + ". Direction: " + pl.getDirections().get(0)
                + System.getProperty("line.separator") + responseType);
        this.createDepartureMsg(departures);

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