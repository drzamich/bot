package bot.processor;

import bot.externalservice.siptw.SipService;
import bot.schema.Departure;
import bot.externalservice.apium.ApiUmTimetableGenerator;
import bot.schema.Platform;
import bot.schema.Response;
import bot.schema.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimetableProcessor extends Settings {
    private List<Station> stations;
    private Optional<Platform> platform = Optional.empty();
    private Optional<List<Departure>> departures;
    private List<String> msg;
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
        this.stations = findStations();

        if(this.stations.size() == 1) {
            this.platform = findPlatform();
        }

        if(this.platform.isPresent()) {
            this.departures = getDepartureInfo();
        }

        return new Response(stations, platform, departures, responseType);
    }

    private List<Station> findStations() {
        StringBuilder sb = new StringBuilder(this.msg.get(0));
        List<Station> res = new ArrayList<>();

        for (int i = 0; i < msg.size(); i++) {
            String str = sb.toString();

            if(stationMap.containsKey(str)) {
                return Arrays.asList(stationMap.get(str));
            }

            List<Station> list = stationMap.entrySet()
                    .stream()
                    .filter(e -> e.getKey().indexOf(str) > -1)
                    .map(Map.Entry::getValue)
                    .distinct()
                    .collect(Collectors.toList());

            if (!list.isEmpty()) {
                res = list;
            }

            if(i<msg.size()-1) sb.append(" "+msg.get(i+1));
        }
        return res;
    }

    private Optional<Platform> findPlatform() {
            return this.stations.get(0).getPlatforms()
                    .stream()
                    .filter(
                        p -> p.getDirections()
                                .stream()
                                .map(Utilities::parseInput)
                                .anyMatch(this.msg::contains)
                                ||
                                this.msg.contains(p.getMainDirection())
                                ||
                                this.msg.contains(p.getNumber())
                    )
                    .findFirst();
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
        ApiUmTimetableGenerator apiUmDepartureService = new ApiUmTimetableGenerator(stations.get(0));
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

    //departureService = new ApiUmTimetableGenerator(this.stations);
}
