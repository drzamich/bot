package bot.externalservice.apium.data;

import lombok.Data;

import java.util.List;

@Data
public class Station {
    private String id;
    private String mainName;
    private List<String> acceptedNames;
    private List<Platform> platforms;

    public Station(String id, String mainName) {
        this.id = id;
        this.mainName = mainName;
    }

    public void addAcceptedName(String acceptedName){
        this.acceptedNames.add(acceptedName);
    }

    public void addPlatform(Platform platform){
        this.platforms.add(platform);
    }
}
