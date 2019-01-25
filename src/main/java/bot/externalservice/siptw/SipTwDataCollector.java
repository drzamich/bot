package bot.externalservice.siptw;

import bot.externalservice.siptw.schema.PlatformSipTw;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Data
public class SipTwDataCollector {
    private final String PATH_TO_PLATFORM_LIST = "src/main/java/bot/externalservice/siptw/data/serialized/platforms";
    private List<PlatformSipTw> platformSipTwList;
    Map<String, PlatformSipTw> platformMap;

    @Autowired
    SipService sipService;

    public SipTwDataCollector() {

    }

    public Map<String, PlatformSipTw> fetchPlatformMap() {
        getPlatformsList();
        parsePlatformsList();
        return this.platformMap;
    }

    public void getPlatformsList() {
        try {
            this.platformSipTwList = sipService.getPlatforms().getPlatformSipTws();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Not able to fetch platforms from SIP TW");
        }
    }

    public void parsePlatformsList() {
        platformMap = new TreeMap<>();
        for (PlatformSipTw platformSipTw : this.platformSipTwList) {
            String name = platformSipTw.getName();
            String platformNumber = name.substring(name.length() - 3, name.length() - 1);
            name = name.substring(0, name.length() - 4);
            String entity = name + platformNumber;
            platformMap.put(entity, platformSipTw);
            System.out.println(platformSipTw);
        }
    }


}
