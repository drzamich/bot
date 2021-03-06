package bot.externalservice.siptw;

import bot.externalservice.siptw.schema.PlatformSipTw;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@Service
public class SipTwDataCollector {
    private List<PlatformSipTw> platformSipTwList;
    private Map<String, PlatformSipTw> platformMap;

    @Autowired
    private SipService sipService;

    public SipTwDataCollector() {
    }

    public Map<String, PlatformSipTw> fetchPlatformMap() {
        getPlatformsList();
        parsePlatformsList();
        return this.platformMap;
    }

    private void getPlatformsList() {
        try {
            SipServiceResponse sipServiceResponse = sipService.getPlatforms();
            this.platformSipTwList = sipServiceResponse.getPlatformSipTws();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Not able to fetch platforms from SIP TW");
        }
    }

    private void parsePlatformsList() {
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
