package bot.externalservice.siptw;

import bot.externalservice.siptw.data.Platform;
import bot.externalservice.siptw.data.PlatformRaw;
import bot.processor.Utilities;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Data
public class SipTwDataCollector {
    private final String PATH_TO_PLATFORM_LIST = "src/main/java/bot/externalservice/siptw/data/serialized/platforms";
    private List<PlatformRaw> platformRawList;
    Map<String, PlatformRaw> platformMap;

    @Autowired
    SipService sipService;

    public SipTwDataCollector() {

    }

    public Map<String, PlatformRaw> fetchPlatformMap() {
        getPlatformsList();
        parsePlatformsList();
        return this.platformMap;
    }

    public void getPlatformsList() {
        try {
            this.platformRawList = sipService.getPlatforms().getPlatforms();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Not able to fetch platforms from SIP TW");
        }
    }

    public void parsePlatformsList() {
        platformMap = new TreeMap<>();
        for (PlatformRaw platformRaw : this.platformRawList) {
            String name = platformRaw.getName();
            String platformNumber = name.substring(name.length() - 3, name.length() - 1);
            name = name.substring(0, name.length() - 4);
            String entity = name + String.valueOf(platformNumber);
            //System.out.println(entity);
            platformMap.put(entity, platformRaw);
            System.out.println(platformRaw);
        }
    }


}
