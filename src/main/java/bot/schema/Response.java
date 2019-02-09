package bot.schema;

import com.github.messenger4j.send.message.quickreply.QuickReply;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Response {
    public static final int MULTIPLE_STATIONS_THRESHOLD = 12;
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
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
        int noOfStations = stations.size();
        if (noOfStations == 0) {
            messages.add("Wrong station name.");
            return;
        } else if (noOfStations > 1 && noOfStations <= MULTIPLE_STATIONS_THRESHOLD) {
            quickRepliesObject = new QuickReplies(stations, "Multiple matching stations. " +
                    "Select the proper one.");
            return;
        } else if (noOfStations > MULTIPLE_STATIONS_THRESHOLD) {
            messages.add("Too many station matching the provided name. Please be more specific.");
            return;
        }
        String stationName = stations.get(0).getMainName();
        messages.add("Departures for: " + stationName);
        if (platforms.size() != 1) {
            quickRepliesObject = new QuickReplies(stations.get(0), "Choose platform:");
            return;
        }
        Platform pl = platforms.get(0);
        messages.add("Leaving from platform " + pl.getNumber() + ". Direction: " + pl.getMainDirection()
                + LINE_SEPARATOR + responseType);
        String depInfo = createDepartureMsg(departures);
        quickRepliesObject = new QuickReplies(depInfo, stationName, pl.getNumber());
    }

    private String createDepartureMsg(Optional<List<Departure>> deps) {
        if (deps.isPresent()) {
            if (deps.get().size() < maxDepartures) {
                maxDepartures = deps.get().size() - 1;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i <= maxDepartures; i++) {
                Departure departure = deps.get().get(i);
                sb.append(departure.getLine()).append(" | ").append(departure.getDirection()).append(" | ").append(departure.getTime());
                if (i <= maxDepartures - 1) {
                    sb.append(LINE_SEPARATOR);
                }
            }
            return sb.toString();
        } else {
            return "No departures from this platform in the nearest future.";
        }
    }

    public String consoleInfo() {
        StringBuilder sb = new StringBuilder();
        //todo simplify
        for (int i = 0; i < messages.size(); i++) {
            sb.append(messages.get(i));
            if (i < messages.size() - 1) {
                sb.append(LINE_SEPARATOR);
            }
        }
        if (quickRepliesObject != null) {
            sb.append(LINE_SEPARATOR).append(quickRepliesObject.toString());
        }
        return sb.toString();
    }
}