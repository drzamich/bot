package bot.schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Response {
    private List<Station> stations;
    private List<Platform> platforms;
    private Optional<List<Departure>> departures;
    private List<String> messages = new ArrayList<>();
    private String consoleInfo;
    private String responseType;
    private List<Button> buttonList;
    private List<String> responseJSONList;
    private String responseJSONString;
    private ButtonList buttonListObject;
    private JSONResponse jsonResponse;


    //How many (max) departures will be displayed in the response (
    private int maxDepartures = 7;

    public Response(List<Station> stations, List<Platform> platforms, Optional<List<Departure>> departures,
                    String responseType) {
        this.stations = stations;
        this.platforms = platforms;
        this.departures = departures;
        this.responseType = responseType;
        prepareMsg();
        prepareConsoleInfo();
        prepareJSON();
    }

    public void prepareJSON() {
        this.jsonResponse = new JSONResponse(this.buttonList, this.messages);
        this.responseJSONList = this.jsonResponse.getResponses();
        this.responseJSONString = this.jsonResponse.convertResponsesToSingleString();

    }

    public void prepareMsg() {
        if (this.stations.size() < 1) {
            this.messages.add("Wrong station.");
            return;
        } else if (this.stations.size() > 1) {
            this.messages.add("Multiple matching stations. Select the proper one.");
            this.buttonListObject = new ButtonList(this.stations);
            this.buttonList = getButtonList();
            return;
        }

        messages.add("Departures for: " + stations.get(0).getMainName());


        if (this.platforms.size() != 1) {
            this.messages.add("Choose platform:");
            this.buttonListObject = new ButtonList(this.stations.get(0));
            this.buttonList = getButtonList();
            return;
        }

        Platform pl = this.platforms.get(0);
        messages.add("Leaving from platform " + pl.getNumber() + ". Direction: " + pl.getMainDirection()
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


    private void prepareConsoleInfo() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.messages.size(); i++) {
            sb.append(this.messages.get(i));
            if (i < this.messages.size() - 1) {
                sb.append(System.getProperty("line.separator"));
            }
        }

        this.consoleInfo = sb.toString();

        if (this.buttonListObject != null) {
            this.consoleInfo += System.getProperty("line.separator") + buttonListObject.toString();
        }
    }

}