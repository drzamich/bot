package bot.processor;

import bot.externalservice.siptw.impl.SipServiceImpl;
import bot.externalservice.siptw.response.SipTwDeparture;
import bot.schema.Departure;
import bot.service.TimetableGenerator;
import bot.schema.Platform;
import bot.schema.LegacyResponse;
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

    private SipServiceImpl sipServiceImpl;

    private DataManager dataManager;

    private TimetableGenerator timetableGenerator;

    @Autowired
    public TimetableProcessor(SipServiceImpl sipServiceImpl, DataManager dataManager, TimetableGenerator timetableGenerator) {
        this.sipServiceImpl = sipServiceImpl;
        this.dataManager = dataManager;
        this.timetableGenerator = timetableGenerator;
    }

    public LegacyResponse processQuery(List<String> msg, int platformNumber) {
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
        return new LegacyResponse(stations, platforms, departures, responseType);
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
            if (i < msg.size() - 1) {
                sb.append(" ").append(msg.get(i + 1));
            }
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
//            if (this.platforms.get(0).isAtSipTw()) {
                this.responseType = "<LIVE>";
                res = getInfoFromSipTw();
//            }
            if (!res.isPresent() || res.get().isEmpty()) {
                this.responseType = "<TIMETABLE>";
                return getInfoFromApiUm();
            }
        }
        return res;
    }

    private Optional<List<Departure>> getInfoFromApiUm() {
        return Optional.of(timetableGenerator.getDeparturesForPlatform(platforms.get(0), stations.get(0)));
    }

    private Optional<List<Departure>> getInfoFromSipTw() {
        // TODO
        return Optional.of(sipServiceImpl
                .getTimetableForPlatform(platforms.get(0).getSipTwID())
                .getDepartures()
        .stream()
        .map(this::toDeparture)
        .collect(Collectors.toList()));
    }

    private Departure toDeparture(SipTwDeparture sipTwDeparture) {
        return new Departure(sipTwDeparture.getLine(), sipTwDeparture.getDestination(), sipTwDeparture.getTimeToArrivalInMinutes());
    }
}
