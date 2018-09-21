package bot.data;

import bot.externalservice.apium.data.Platform;
import lombok.Data;

import java.util.List;

@Data
public class PlatformDepartureInfo {
    private List<Departure> departures;
    private Platform platform;

    public PlatformDepartureInfo(Platform platform, List<Departure> departures) {
        this.platform = platform;
        this.departures = departures;
    }
}
