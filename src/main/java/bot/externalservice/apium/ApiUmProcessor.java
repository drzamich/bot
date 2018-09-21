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
        System.out.println("ApiUMProcessor");
        String[] msg = query.getBodyExploded();

        StationService stationService = new StationService(msg);

        if(stationService.isStationExists()){
            bot.data.Station station = new bot.data.Station(stationService.getStationName());
            if (stationService.isPlatformExists()) {
                List<Platform> platformList = new ArrayList<>();

                for (bot.externalservice.apium.data.Platform platform : stationService.getPlatforms()) {
                    platformList.add(new Platform(platform.getNumber(), platform.getDirection()));
                }
                if (stationService.isDepartuesExist()) {
                    return new Response(station, platformList, stationService.getPlatformDepartureInfos());
                }
                return new Response(station, platformList);
            }
            return new Response(station);
        }
        else {
            return new Response();
        }
    }
}
