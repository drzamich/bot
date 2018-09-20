package bot.externalservice.apium;

import bot.externalservice.apium.data.DataManager;
import bot.externalservice.apium.data.DepartureDetail;
import bot.externalservice.apium.data.Platform;
import bot.externalservice.apium.data.Station;
import bot.processor.Utilities;
import org.apache.commons.collections4.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class StationService extends DataManager {
    private Station station;
    private String stationName;
    private String stationId;
    private List<Platform> platforms;

    public StationService() {
        this.stationsMap = deserializeStationMap();
    }

    @Autowired
    ApiUmService apiUmService = new ApiUmService(new RestTemplateBuilder());

    public void processMsg(String msg) {
        msg = Utilities.parseInput(msg);
        System.out.println(msg);
        Optional<Station> stationOpt = findStation(msg);

        if (stationOpt.isPresent()) {
            this.station = stationOpt.get();
            this.processStation();
        } else {
            System.out.println("Wrong name");
        }
    }

    private void processStation() {
        this.stationName = this.station.getMainName();
        this.stationId = this.station.getId();
        this.platforms = this.station.getPlatforms();
        for (Platform platform : this.platforms) {
            this.processPlatform(platform);
            return;
        }
        System.out.println(this.station);
    }

    private void processPlatform(Platform platform) {
        String platformNumber = platform.getNumber();
        List<String> lines = platform.getLines();
        MultiValueMap<String, DepartureDetail> mappedDepartures = new MultiValueMap<>();
        List<String> times = new ArrayList<>();
        for (String line : lines) {
            List<DepartureDetail> list = apiUmService.getDepartureDetails(this.stationId, platformNumber, line);
            for (DepartureDetail departureDetail : list) {
                String time = departureDetail.getTime();
                mappedDepartures.put(time, departureDetail);

                if (!times.contains(time)) {
                    times.add(time);
                }
            }
        }
        String timePattern = "HH:mm:ss";
        Collections.sort(times);
        String currTime = Utilities.getTime(timePattern);

        for (String time : times) {
            long timeDiff = Utilities.compareTimes(currTime, time, timePattern);
            if (timeDiff >= 0) {
                int minDiff = Math.round(timeDiff/60000);
                System.out.println(mappedDepartures.get(time) + " "+Long.toString(timeDiff)+" "+Integer.toString(minDiff));
            }
        }
    }

//
//        Map<String,DepartureDetail> realMappedDepartures = mappedDepartures.asMap();
//        for(String key : mappedDepartures.keySet()){
//
//        }
//        System.out.println(mappedDepartures);


    private Optional<Station> findStation(String proposedName) {
        return Optional.ofNullable(this.stationsMap.get(proposedName));
    }
}
