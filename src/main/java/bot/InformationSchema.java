package bot;

import lombok.Data;

@Data
public class InformationSchema {
    private String stationName;
    private int platformId;
    private String platformMainDir;

    public InformationSchema(Station station, Platform platform) {
        this.stationName = station.getStationName();
        this.platformId = platform.getPlatformId();
        this.platformMainDir = platform.getMainDir();
    }

    @Override
    public String toString() {
        return "Station name: " + this.stationName + "\n" +
                "Direction: " + this.platformMainDir + "\n" +
                "Platform ID:" + this.platformId;
    }
}
