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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimetableProcessor extends Settings {
    private List<Station> stations = new ArrayList<>();
    private List<Platform> platforms = new ArrayList<>();
    private Optional<List<Departure>> departures;
    private List<String> msg;
    private Map<String, Station> stationMap;
    private String responseType;
    private int platformNumber;

    @Autowired
    private SipService sipService;

    @Autowired
    private DataManager dataManager;

    public TimetableProcessor() {
    }


    protected Response processQuery(Query query) {
        this.stationMap = dataManager.getFinalMap();

        this.msg = query.getBodyExploded();
        this.platformNumber = query.getLastNumber();

        this.stations = findStations();

        if(this.stations.size() == 1) {
            this.platforms = findPlatforms();
        }

        if(this.platforms.size() == 1) {
            this.departures = getDepartureInfo();
        }

        return new Response(stations, platforms, departures, responseType);
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

    private List<Platform> findPlatforms() {
        List<Platform> res = new ArrayList<>();

        for(Platform p: stations.get(0).getPlatforms()) {
            List<String> dirs = p.getDirections();
            int plNum = Integer.valueOf(p.getNumber());

            if(plNum == this.platformNumber || !CollectionUtils.intersection(dirs,this.msg).isEmpty()) {
                res.add(p);
            }
        }
        return res;
    }


    private Optional<List<Departure>> getDepartureInfo() {
        Optional<List<Departure>> res = Optional.empty();
        if (this.platforms.size()==1) {
            if (this.platforms.get(0).isAtSipTw()) {
                res = getInfoFromSipTw();
            }
            if (!res.isPresent()) {
                this.responseType = "<TIMETABLE>";
                return getInfoFromApiUm();
            }
        }
        this.responseType = "<LIVE>";
        return res;
    }

    private Optional<List<Departure>> getInfoFromApiUm() {
        ApiUmTimetableGenerator apiUmDepartureService = new ApiUmTimetableGenerator(stations.get(0));
        return Optional.of(apiUmDepartureService.getDeparturesForPlatform(platforms.get(0)));
    }

    private Optional<List<Departure>> getInfoFromSipTw() {
//        sipService = new SipService(new RestTemplateBuilder());
        try {
            return Optional.ofNullable(sipService.getTimetableForPlatform(platforms.get(0).getSipTwID()).getDepartures());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Not possible ");
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
