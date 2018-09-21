package bot.externalservice.apium;

import bot.data.Platform;
import bot.externalservice.apium.data.Station;
import bot.processor.Query;
import bot.processor.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ApiUmProcessor {
    @Autowired
    private Station station;
    Response response;

    public Response processQuery(Query query){
        String[] msg = query.getBodyExploded();

        StationService stationService = new StationService(msg);

        if(stationService.isStationExists()){
            List<Platform> platformList = new ArrayList<>();
            for (bot.externalservice.apium.data.Platform platform : stationService.getStation().getPlatforms()) {
                platformList.add(new Platform(platform.getNumber(), platform.getDirection()));
            }
            bot.data.Station station = new bot.data.Station(stationService.getStationName(),platformList);
            if (stationService.isPlatformExists()) {
                if (stationService.isDeparturesExist()) {
                    return new Response(station, platformList, stationService.getPlatformDepartureInfos());
                }
            }
            return new Response(station, platformList);
        }
        else {
            return new Response();
        }
    }
}
