package bot.externalservice.apium.data;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class StationsMap implements Serializable {
    private Map<String,Station> stationsMap;

    public StationsMap(Map<String,Station> stationsMap) {
        this.stationsMap = stationsMap;
    }
}
