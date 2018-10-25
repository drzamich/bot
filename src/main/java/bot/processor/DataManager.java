package bot.processor;

import bot.externalservice.apium.ApiUmDataCollector;
import bot.schema.Platform;
import bot.schema.Station;
import bot.externalservice.siptw.SipTwDataCollector;
import bot.externalservice.siptw.data.PlatformRaw;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Data
public class DataManager extends Settings {

    //@Autowired
    SipTwDataCollector sipTwDataCollector = new SipTwDataCollector();

    //@Autowired
    ApiUmDataCollector apiUmDataCollector = new ApiUmDataCollector();


    public DataManager() {
        prepareData();
    }

    public void prepareData() {
        prepareStationMap();
        //downloadTimetables();
    }


    public void prepareStationMap() {
        if (!Utilities.objectExists(PATH_TO_STATION_MAP)) {
            Map<String, PlatformRaw> sipTwPlatformMap = sipTwDataCollector.getPlatformMap();
            List<Station> umStationList = apiUmDataCollector.getStationList();

            this.stationMap = integrateLists(sipTwPlatformMap,umStationList);
            Utilities.serializeObject(stationMap,PATH_TO_STATION_MAP);
        }
    }


    public Map<String,Station> integrateLists(Map<String,PlatformRaw> sipTwPlatformMap, List<Station> umStationList) {
        for (Station station : umStationList) {
            String stationName = station.getMainName();
            for (Iterator<Platform> i = station.getPlatforms().iterator(); i.hasNext(); ) {
                Platform platform = i.next();
                String number = platform.getNumber();
                String validator = stationName + " " + number;
                if (sipTwPlatformMap.containsKey(validator)) {
                    int SipTwId = Integer.valueOf(sipTwPlatformMap.get(validator).getInnerId());
                    platform.setAtSipTw(true);
                    platform.setSipTwID(SipTwId);
                }
            }
        }

        Map<String,Station> stationMap = new HashMap<>();
        for(Station station: umStationList){
            for(String accName : station.getAcceptedNames()){
                stationMap.put(accName,station);
            }
        }
        return stationMap;
    }


}
