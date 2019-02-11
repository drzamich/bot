package bot.schema;

import bot.externalservice.siptw.schema.DepartureSipTw;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Departure implements Serializable {

    private String line;
    private String direction;
    private String time;

    public static Departure fromSipTwDeparture(DepartureSipTw departureSipTw) {
        return new Departure(departureSipTw.getLine(), departureSipTw.getDestination(), departureSipTw.getTimeMinutes());
    }
}
