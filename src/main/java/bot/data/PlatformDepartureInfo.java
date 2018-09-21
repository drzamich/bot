package bot.data;

import lombok.Data;

import java.util.List;

@Data
public class PlatformDepartureInfo {
    private String number;
    private String direction;
    private List<Departure> departures;

    public PlatformDepartureInfo(String number, String direction, List<Departure> departures) {
        this.number = number;
        this.direction = direction;
        this.departures = departures;
    }
}
