
package bot.externalservice.apium;

import bot.externalservice.apium.data.Station;
import bot.processor.Query;
import bot.processor.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ApiUmProcessor {

    public Response processQuery(Query query) {
        String[] msg = query.getBodyExploded();

        StationService stationService = new StationService(Arrays.asList(msg));

        return new Response(stationService.getStation(), stationService.getPlatforms(), stationService.getPlatformDepartureInfos());
    }
}
