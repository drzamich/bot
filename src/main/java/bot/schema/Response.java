package bot.schema;

import com.github.messenger4j.send.message.quickreply.QuickReply;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Data
public class Response {
    private List<Station> stations;
    private List<Platform> platforms;
    private Optional<List<Departure>> departures;
    private List<String> messages = new ArrayList<>();
    private String consoleInfo;
    private String responseType;
    private QuickReplies quickRepliesObject;

    private List<QuickReply> quickReplies;

    //How many (max) departures will be displayed in the response (
    private int maxDepartures = 7;

    public Response(List<Station> stations, List<Platform> platforms, Optional<List<Departure>> departures,
                    String responseType) {
        this.stations = stations;
        this.platforms = platforms;
        this.departures = departures;
        this.responseType = responseType;
        prepareMsg();
    }

    public void prepareMsg() {
        if (this.stations.size() < 1) {
            this.messages.add("Wrong station name.");
            return;
        } else if (this.stations.size() > 1 && this.stations.size() < 12) {
            this.quickRepliesObject = new QuickReplies(this.stations, "Multiple matching stations. " +
                    "Select the proper one.");
            return;
        } else if (this.stations.size() > 11) {
            this.messages.add("Too many station matching the provided name. Please be more specific.");
            return;
        }

        String stationName = stations.get(0).getMainName();

        messages.add("Departures for: " + stationName);


        if (this.platforms.size() != 1) {
            this.quickRepliesObject = new QuickReplies(this.stations.get(0), "Choose platform:");
            return;
        }

        Platform pl = this.platforms.get(0);
        messages.add("Leaving from platform " + pl.getNumber() + ". Direction: " + pl.getMainDirection()
                + System.getProperty("line.separator") + responseType);

        String depInfo = createDepartureMsg(departures);

        this.quickRepliesObject = new QuickReplies(depInfo, stationName, pl.getNumber());
    }


    private String createDepartureMsg(Optional<List<Departure>> deps) {
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
            return sb.toString();
        } else {
            return "No departures from this platform in the nearest future.";
        }
    }


    public String consoleInfo() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.messages.size(); i++) {
            sb.append(this.messages.get(i));
            if (i < this.messages.size() - 1) {
                sb.append(System.getProperty("line.separator"));
            }
        }

        if(this.quickRepliesObject != null) {
            sb.append(System.getProperty("line.separator") + quickRepliesObject.toString());
        }

        return sb.toString();
    }

}