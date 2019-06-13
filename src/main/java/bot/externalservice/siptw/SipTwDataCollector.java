package bot.externalservice.siptw;

import bot.externalservice.siptw.response.SipTwPlatform;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@Service
@Slf4j
public class SipTwDataCollector {

    private SipService sipService;

    @Autowired
    public SipTwDataCollector(SipService sipService) {
        this.sipService = sipService;
    }

    public Map<String, SipTwPlatform> fetchPlatformMap() {
        List<SipTwPlatform> sipTwPlatforms = sipService.getPlatforms().getSipTwPlatforms();
        Map<String, SipTwPlatform> platformMap = new TreeMap<>(); //TODO czemu treemapa?
        for (SipTwPlatform sipTwPlatform : sipTwPlatforms) {
            String name = sipTwPlatform.getPlatformName();
            String platformNumber = StringUtils.substringBetween(name, "[", "]");
            name = name.substring(0, name.indexOf(" ["));
            String lifelikePlatformName = name + StringUtils.SPACE + platformNumber;
            platformMap.put(lifelikePlatformName, sipTwPlatform);
        }
        return platformMap;
    }

}
