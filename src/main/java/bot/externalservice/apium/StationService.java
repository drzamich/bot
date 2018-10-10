package bot.externalservice.apium;

import bot.data.Departure;
import bot.data.PlatformDepartureInfo;
import bot.externalservice.apium.data.*;
import bot.processor.Utilities;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Data
public class StationService extends DataManager {
    private Station station;
    private List<Platform> platforms;
    private List<PlatformDepartureInfo> platformDepartureInfos = new ArrayList<>();
    private List<String> msg;
    private DepartureService departureService;


    public StationService(List<String> msg) {
        super();
        this.msg = msg;
        this.findStation();
        this.findPlatforms();
        this.processPlatforms();
    }

    public StationService(Station station) {
        super();
        this.station = station;
        departureService = new DepartureService(this.station);
        this.platforms = station.getPlatforms();
        this.processPlatforms();
    }

    public void findStation() {
        Map<String,Station> stationMap = (Map<String,Station>) Utilities.deserializeObject(this.pathToStationMap);
        Station stationSaved = null; //this is to ensure that the name is not matched too early
        StringBuilder s = new StringBuilder(this.msg.get(0));
        for (int i = 0; i < this.msg.size(); i++) {
            if (i != 0) s.append(this.msg.get(i));
            Optional<Station> stationOpt = Optional.ofNullable(stationMap.get(s.toString()));
            if (stationOpt.isPresent()) {
                stationSaved = stationOpt.get();
            }
        }
        this.station = stationSaved;
        departureService = new DepartureService(this.station);
    }

    public void findPlatforms() {
        if (this.station != null) {
            List<Platform> platforms = this.station.getPlatforms();
            List<Platform> res = new ArrayList<>();
            for (Platform platform : platforms) {
                String platformNumber = platform.getNumber();
                String direction = Utilities.parseInput(platform.getDirection());

                if (this.msg.contains("all")) {
                    res = platforms;
                }
                if (this.msg.contains(platformNumber) || this.msg.contains(direction)) {
                    res.add(platform);
                }
            }
            this.platforms = res;
        }
    }


    public void processPlatforms() {

        this.platforms.parallelStream()
                .forEach(s -> this.processPlatform(s));
//        for (Platform platform : this.platforms) {
//
//        }
    }

    public void processPlatform(Platform platform){
        List<Departure> departureList = this.departureService.getDeparturesForPlatform(platform);
        Platform pl = new Platform(platform.getNumber(), platform.getDirection());
        this.platformDepartureInfos.add(new PlatformDepartureInfo(pl, departureList));
    }

}

