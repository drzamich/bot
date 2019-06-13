package bot.externalservice.ztm.response;

import lombok.Data;

import java.util.List;

@Data
public class ZtmStation {

    private String id;

    private String mainName;

    private List<ZtmPlatform> platforms;

    private String urlToPlatforms;

    public ZtmStation(String id, String stationName, String url) {
        this.id = id;
        this.mainName = stationName;
        this.urlToPlatforms = url;
    }
}
