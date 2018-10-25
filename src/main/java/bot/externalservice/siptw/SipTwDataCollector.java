package bot.externalservice.siptw;

import bot.externalservice.siptw.data.PlatformRaw;
import bot.processor.Utilities;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Data
public class SipTwDataCollector {
    private final String PATH_TO_PLATFORM_LIST = "src/main/java/bot/externalservice/siptw/data/serialized/platforms";
    private List<PlatformRaw> platformRawList;
    Map<String, PlatformRaw> platformMap;

    @Autowired
    SipService sipService;

    public SipTwDataCollector(){
        getPlatformsList();
        parsePlatformsList();
    }
//    public void process() {
//
//    }

    public void getPlatformsList() {
        if (!Utilities.objectExists(PATH_TO_PLATFORM_LIST)) {
            try {
                this.platformRawList = sipService.getPlatforms().getPlatforms();
                Utilities.serializeObject(this.platformRawList, PATH_TO_PLATFORM_LIST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.platformRawList = Utilities.deserializeObject(PATH_TO_PLATFORM_LIST);
        }
    }

    public void parsePlatformsList() {
        platformMap = new HashMap<>();
        for (PlatformRaw platformRaw : this.platformRawList) {
            String name = platformRaw.getName();
            String platformNumber = name.substring(name.length() - 3, name.length() - 1);
            name = name.substring(0, name.length() - 4);
            String entity = name + String.valueOf(platformNumber);
            //System.out.println(entity);
            platformMap.put(entity, platformRaw);

        }

    }


}
