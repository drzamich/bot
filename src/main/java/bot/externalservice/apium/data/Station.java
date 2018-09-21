package bot.externalservice.apium.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Station implements Serializable {
    private String id;
    private String mainName;
    private List<String> acceptedNames;
    private List<Platform> platforms;
    private String urlToPlatforms;

    public Station() {
    }

    public Station(String id, String mainName, String url) {
        this.id = id;
        this.mainName = mainName;
        this.urlToPlatforms = url;
    }

    public void addAcceptedName(String acceptedName){
        this.acceptedNames.add(acceptedName);
    }

    public void addPlatform(Platform platform){
        this.platforms.add(platform);
    }
}
