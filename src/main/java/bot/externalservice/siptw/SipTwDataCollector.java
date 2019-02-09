package bot.externalservice.siptw;

import bot.externalservice.siptw.response.SipServicePlatformResponse;
import bot.externalservice.siptw.schema.PlatformSipTw;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@Service
@Slf4j
public class SipTwDataCollector {
    private List<PlatformSipTw> platformSipTwList;
    private Map<String, PlatformSipTw> platformMap;


    private SipService sipService;

    @Autowired
    public SipTwDataCollector(SipService sipService) {
        this.sipService = sipService;
    }

    public Map<String, PlatformSipTw> fetchPlatformMap() {
        getPlatformsList();
        parsePlatformsList();
        return this.platformMap;
    }

    private void getPlatformsList() {
        try {
            SipServicePlatformResponse sipServiceDepartureResponse = sipService.getPlatforms();
            this.platformSipTwList = sipServiceDepartureResponse.getPlatformSipTws();
        } catch (Exception e) {
            log.error("Not able to fetch platforms from SIP TW", e);
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
        }
    }


}
