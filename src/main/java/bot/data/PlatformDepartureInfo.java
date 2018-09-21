package bot.data;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class PlatformDepartureInfo {
    private String number;
    private String direction;
    private Optional<List<Departure>> departures;

    public PlatformDepartureInfo(String number, String direction, Optional<List<Departure>> departures) {
        this.number = number;
        this.direction = direction;
        this.departures = departures;
    }
}
