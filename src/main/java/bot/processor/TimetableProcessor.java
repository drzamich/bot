package bot.processor;

import bot.externalservice.siptw.SipService;
import bot.schema.Departure;
import bot.externalservice.apium.ApiUmTimetableGenerator;
import bot.schema.Platform;
import bot.schema.Response;
import bot.schema.Station;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.*;

@Service
public class TimetableProcessor extends Settings {
    private Optional<Station> station;
    private Optional<Platform> platform;
    private Optional<List<Departure>> departures;
    private List<String> msg;
    private List<Station> stationList;
    private Map<String, Station> stationMap;
    private String responseType;

    @Autowired
    SipService sipService;

    public TimetableProcessor() {
        DataManager dataManager = new DataManager();
        this.stationMap = dataManager.getFinalMap();
    }


    protected Response processQuery(Query query) {
        this.msg = query.getBodyExploded();
        this.station = findStation();
        this.platform = findPlatform();
        this.departures = getDepartureInfo();
        return new Response(station, platform, departures, responseType);
    }

    private Optional<Station> findStation() {
        Optional<Station> stationSaved = Optional.empty(); //this is to ensure that the name is not matched too early
        StringBuilder s = new StringBuilder(msg.get(0));
        for (int i = 0; i < msg.size(); i++) {
            if (i != 0) s.append(msg.get(i));
            Optional<Station> stationOpt = Optional.ofNullable(stationMap.get(s.toString()));
            if (stationOpt.isPresent()) {
                stationSaved = stationOpt;
            }
        }
        return stationSaved;
    }

    private Optional<Platform> findPlatform() {
        if (this.station.isPresent()) {
            for (Platform platform : this.station.get().getPlatforms()) {

                List<String> directions = new ArrayList<>();
                directions.add(Utilities.parseInput(platform.getMainDirection()));
                for (String d : platform.getDirections()) {
                    directions.add(Utilities.parseInput(d));
                }

                // Check if the any of direction available for platform is contained in the message
                Collection<String> intersection = CollectionUtils.intersection(this.msg, directions);

                String platformNumber = platform.getNumber();

                if (this.msg.contains(platformNumber) || intersection.size() > 0) {
                    return Optional.of(platform);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<List<Departure>> getDepartureInfo() {
        Optional<List<Departure>> res = Optional.empty();
        if (platform.isPresent()) {
            if (platform.get().isAtSipTw()) {
                res = getInfoFromSipTw();
            }
            if (!res.isPresent()) {
                responseType = "<TIMETABLE>";
                return getInfoFromApiUm();
            }
        }
        responseType = "<LIVE>";
        return res;
    }

    private Optional<List<Departure>> getInfoFromApiUm() {
        ApiUmTimetableGenerator apiUmDepartureService = new ApiUmTimetableGenerator(station.get());
        return Optional.of(apiUmDepartureService.getDeparturesForPlatform(platform.get()));
    }

    private Optional<List<Departure>> getInfoFromSipTw() {
        try {
            return Optional.ofNullable(sipService.getTimetableForPlatform(platform.get().getSipTwID()).getDepartures());
        } catch (Exception e) {
            //e.printStackTrace();
            return Optional.empty();
        }
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    //departureService = new ApiUmTimetableGenerator(this.station);
}
