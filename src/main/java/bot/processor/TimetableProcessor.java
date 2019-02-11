package bot.processor;

import bot.externalservice.siptw.SipService;
import bot.schema.Departure;
import bot.externalservice.apium.ApiUmTimetableGenerator;
import bot.schema.Platform;
import bot.schema.Response;
import bot.schema.Station;
import bot.utils.StringHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimetableProcessor {
    private List<Station> stations = new ArrayList<>();
    private List<Platform> platforms = new ArrayList<>();
    private Optional<List<Departure>> departures;
    private List<String> msg;
    private Map<String, Station> stationMap;
    private String responseType;
    private int platformNumber;

    private SipService sipService;

    private DataManager dataManager;

    private ApiUmTimetableGenerator apiUmTimetableGenerator;

    @Autowired
    public TimetableProcessor(SipService sipService, DataManager dataManager, ApiUmTimetableGenerator apiUmTimetableGenerator) {
        this.sipService = sipService;
        this.dataManager = dataManager;
        this.apiUmTimetableGenerator = apiUmTimetableGenerator;
    }

    public Response processQuery(List<String> msg, int platformNumber) {
        this.stationMap = dataManager.getFinalMap();
        this.msg = msg;
        this.platformNumber = platformNumber;
        this.stations = findStations();
        if (this.stations.size() == 1) {
            this.platforms = findPlatforms();
        }
        if (this.platforms.size() == 1) {
            this.departures = getDepartureInfo();
        }
        return new Response(stations, platforms, departures, responseType);
    }

    private List<Station> findStations() {
        StringBuilder sb = new StringBuilder(this.msg.get(0));
        List<Station> res = new ArrayList<>();
        for (int i = 0; i < msg.size(); i++) {
            String str = sb.toString();
            if (stationMap.containsKey(str)) {
                return Collections.singletonList(stationMap.get(str));
            }
            List<Station> list = stationMap.entrySet()
                    .stream()
                    .filter(e -> e.getKey().contains(str))
                    .map(Map.Entry::getValue)
                    .distinct()
                    .collect(Collectors.toList());
            if (!list.isEmpty()) {
                res = list;
            }
            if (i < msg.size() - 1) sb.append(" ").append(msg.get(i + 1));
        }
        return res;
    }

    private List<Platform> findPlatforms() {
        List<Platform> res = new ArrayList<>();
        for (Platform p : stations.get(0).getPlatforms()) {
            List<String> dirs = p.getDirections().stream()
                    .map(StringHelper::sanitizeInput)
                    .collect(Collectors.toList());
            int plNum = Integer.valueOf(p.getNumber());
            if (plNum == this.platformNumber || !CollectionUtils.intersection(dirs, this.msg).isEmpty()) {
                res.add(p);
            }
        }
        return res;
    }

    private Optional<List<Departure>> getDepartureInfo() {
        Optional<List<Departure>> res = Optional.empty();
        if (this.platforms.size() == 1) {
            if (this.platforms.get(0).isAtSipTw()) {
                this.responseType = "<LIVE>";
                res = getInfoFromSipTw();
            }
            if (!res.isPresent() || res.get().isEmpty()) {
                this.responseType = "<TIMETABLE>";
                return getInfoFromApiUm();
            }
        }
        return res;
    }

    private Optional<List<Departure>> getInfoFromApiUm() {
        return Optional.of(apiUmTimetableGenerator.getDeparturesForPlatform(platforms.get(0), stations.get(0)));
    }

    private Optional<List<Departure>> getInfoFromSipTw() {
        return Optional.ofNullable(sipService.getTimetableForPlatform(platforms.get(0).getSipTwID()).getDepartures());
    }
}
