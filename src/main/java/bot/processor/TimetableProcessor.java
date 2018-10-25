package bot.processor;

import bot.data.Departure;
import bot.externalservice.apium.ApiUmDepartureService;
import bot.externalservice.apium.data.Platform;
import bot.externalservice.apium.data.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimetableProcessor extends Settings {
    private Optional<Station> station;
    private Optional<Platform> platform;
    private Optional<List<Departure>> departures;
    private List<String> msg;

    public TimetableProcessor() {
        this.stationMap = Utilities.deserializeObject(PATH_TO_STATION_MAP);
    }

    protected void processQuery(Query query) {
        this.msg = query.getBodyExploded();
        this.station = findStation();
        this.platform = findPlatform();
        this.departures = getDepartureInfo();
        System.out.println(station);
        System.out.println(platform);
        System.out.println(departures);
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
        if(this.station.isPresent()) {
            for (Platform platform : this.station.get().getPlatforms()) {
                String platformNumber = platform.getNumber();
                String direction = Utilities.parseInput(platform.getDirection());

                if (this.msg.contains(platformNumber) || this.msg.contains(direction)) {
                    return Optional.of(platform);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<List<Departure>> getDepartureInfo(){
        if(platform.isPresent()){
            if(platform.get().isAtSipTw()){
                //return getInfoFromSipTw();
            }
            else {
                return getInfoFromApiUm();
            }
        }
        return Optional.empty();
    }

    private Optional<List<Departure>> getInfoFromApiUm(){
        ApiUmDepartureService apiUmDepartureService = new ApiUmDepartureService(station.get());
        return Optional.of(apiUmDepartureService.getDeparturesForPlatform(platform.get()));
    }

    //departureService = new ApiUmDepartureService(this.station);
}
