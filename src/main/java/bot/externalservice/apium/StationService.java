package bot.externalservice.apium;

import bot.data.Departure;
import bot.data.PlatformDepartureInfo;
import bot.externalservice.apium.data.*;
import bot.processor.Utilities;
import lombok.Data;
import org.apache.commons.collections4.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Data
public class StationService extends DataManager {
    private Station station;
    private String stationName;
    private String stationId;
    private List<Platform> platforms = new ArrayList<>();
    private List<PlatformDepartureInfo> platformDepartureInfos = new ArrayList<>();
    private String date;
    private final int MAX_MINUTES = 20;
    private String[] msg;

    private boolean stationExists = false;
    private boolean platformExists = false;
    private boolean departuesExist = false;


    public StationService(String[] msg) {
        super();
        this.stationsMap = Utilities.deserializeObject(this.pathToStationMap);
        this.msg = msg;
        this.findStation();
        this.findPlatforms();
        this.processPlatforms();
    }

    @Autowired
    ApiUmService apiUmService = new ApiUmService(new RestTemplateBuilder());

    public void findStation() {
        String proposedName = this.msg[0];
        Optional<Station> stationOptSaved = Optional.empty(); //this is to ensure that the name is not matched too early
        for(int i = 0;i< this.msg.length;i++){
            Optional<Station> stationOpt = Optional.ofNullable(this.stationsMap.get(proposedName));
            if(stationOpt.isPresent()){
                stationOptSaved = stationOpt;
            }
        }
        if(stationOptSaved.isPresent()){
            this.station = stationOptSaved.get();
            this.stationId = this.station.getId();
            this.stationExists = true;
        }
    }

    public void findPlatforms(){
        if(this.stationExists) {
            for (Platform platform : this.station.getPlatforms()) {
                for (String s : this.msg) {
                    if (s.equals("all")) {
                        this.platforms = this.station.getPlatforms();
                        this.platformExists = true;
                        return;
                    }
                    if (s.equals(platform.getNumber())) {
                        this.platforms.add(platform);
                        this.platformExists = true;
                    }
                }
            }
        }
    }

    public void processPlatforms(){
        if(this.isPlatformExists()) {
            for (Platform platform : this.platforms) {
                Optional<List<Departure>> departureList = getDeparturesForPlatform(platform);
                if (departureList.isPresent()) {
                    this.platformDepartureInfos.add(new PlatformDepartureInfo(platform.getNumber(), platform.getDirection(), departureList.get()));
                    this.departuesExist = true;
                }
            }
        }
    }


    private Optional<List<Departure>> getDeparturesForPlatform(Platform platform) {
        String platformNumber = platform.getNumber();

        String identifier = this.date + "_" + this.stationId + "_" + platformNumber;
        String path = PATH_TO_OBJECTS + identifier;
        Optional<DeparturesListRaw> departuresListRaw;

        if (Utilities.objectExists(path)) {
            departuresListRaw = Optional.of(Utilities.deserializeObject(path));
        } else {
            departuresListRaw = getDeparturesListRaw(platform, path);
        }

        if(departuresListRaw.isPresent()){
            return Optional.of(this.sortDeparturesList(departuresListRaw.get()));
        }
        else {
            return Optional.empty();
        }
    }



    private List<Departure> sortDeparturesList(DeparturesListRaw departuresListRaw){
        List<Departure> departuresListSorted = new ArrayList<>();

        List<String> times = departuresListRaw.getTimes();
        MultiValueMap<String, DepartureDetail> mappedDepartures = departuresListRaw.getMappedDepartures();
        String currTime = Utilities.getTime(TIME_PATTERN);
        for (String time : times) {
            int timeDiff = Math.round(Utilities.compareTimes(currTime, time, TIME_PATTERN) / 60000);
            if (timeDiff >= 0 /*&& timeDiff < MAX_MINUTES*/) {
                List<DepartureDetail> deps = (List<DepartureDetail>) mappedDepartures.get(time);
                for(DepartureDetail dep: deps){
                    Departure departure = new Departure(dep.getLine(),dep.getDirection(),Integer.toString(timeDiff));
                    departuresListSorted.add(departure);
                }
            }
        }
        return departuresListSorted;
    }

    private Optional<DeparturesListRaw> getDeparturesListRaw(Platform platform, String path) {
        String platformNumber = platform.getNumber();
        List<String> lines = platform.getLines();
        MultiValueMap<String, DepartureDetail> mappedDepartures = new MultiValueMap<>();
        List<String> times = new ArrayList<>();

        for (String line : lines) {
            Optional<List<DepartureDetail>> list = apiUmService.getDepartureDetails(this.stationId, platformNumber, line);
            if(list.isPresent()) {
                for (DepartureDetail departureDetail : list.get()) {
                    String time = departureDetail.getTime();
                    mappedDepartures.put(time, departureDetail);
                    if (!times.contains(time)) {
                        times.add(time);
                    }
                }
            }
        }

        if(mappedDepartures.isEmpty() || times.isEmpty()){
            return Optional.empty();
        }

        Collections.sort(times);
        DeparturesListRaw departuresListRaw = new DeparturesListRaw(times, mappedDepartures);
        Utilities.serializeObject(departuresListRaw, path);
        return Optional.of(departuresListRaw);
    }
}
