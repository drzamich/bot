package bot.processor;

import bot.externalservice.apium.ApiUmDataCollector;
import bot.externalservice.apium.data.Platform;
import bot.externalservice.apium.data.Station;
import bot.externalservice.siptw.SipTwDataCollector;
import bot.externalservice.siptw.data.PlatformRaw;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Data
public class DataManager {
    private Map<String,PlatformRaw> sipTwPlatformMap;
    private List<Station> umStationList;

    private List<Station> incorporatedList;

    private String date = Utilities.getTime("yyyyMMdd");
    private final String PATH_TO_STATION_LIST = "src/main/java/bot/data/serialized/"+date+"_station_list";

    //@Autowired
    SipTwDataCollector sipTwDataCollector = new SipTwDataCollector();

    //@Autowired
    ApiUmDataCollector apiUmDataCollector = new ApiUmDataCollector();

    public List<Station> getStationList(){
        if(!Utilities.objectExists(PATH_TO_STATION_LIST)){
            prepareLists();
            incorporateLists();
            Utilities.serializeObject(umStationList,PATH_TO_STATION_LIST);
            return umStationList;
        }
        else {
            return Utilities.deserializeObject(PATH_TO_STATION_LIST);
        }
    }

    public void prepareLists(){
        sipTwDataCollector.process();
        sipTwPlatformMap = sipTwDataCollector.getPlatformMap();

        apiUmDataCollector.getStationList();
        umStationList = apiUmDataCollector.getList();
    }

    public void incorporateLists(){
        for(Station station: umStationList){
            String stationName = station.getMainName();
            for(Iterator<Platform> i = station.getPlatforms().iterator(); i.hasNext();){
                Platform platform = i.next();
                String number = platform.getNumber();
                String validator = stationName+" "+number;
                if(sipTwPlatformMap.containsKey(validator)){
                    int SipTwId = Integer.valueOf(sipTwPlatformMap.get(validator).getInnerId());
                    platform.setAtSipTw(true);
                    platform.setSipTwID(SipTwId);
                }
            }
        }
    }


}
