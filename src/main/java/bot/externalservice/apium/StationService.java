package bot.externalservice.apium;

import bot.externalservice.apium.data.DataManager;
import bot.externalservice.apium.data.Platform;
import bot.externalservice.apium.data.Station;
import bot.processor.Utilities;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StationService extends DataManager {
    private Station station;
    private String stationName;
    private String stationId;
    private List<Platform> platforms;

    public StationService() {
        this.stationsMap = deserializeStationMap();
    }

    public void processMsg(String msg){
        msg = Utilities.parseInput(msg);
        System.out.println(msg);
        Optional <Station> stationOpt = findStation(msg);

        if(stationOpt.isPresent()){
            this.station = stationOpt.get();
            this.processStation();
        }
        else {
            System.out.println("Wrong name");
        }
    }

    private void processStation(){
        this.stationName = this.station.getMainName();
        this.stationId = this.station.getId();
        this.platforms = this.station.getPlatforms();
        for(Platform platform: this.platforms){
            this.processPlatform(platform);
        }
        System.out.println(this.station);
    }

    private void processPlatform(Platform platform){
        String platformNumber = platform.getNumber();
        List<String> lines = platform.getLines();
        for (String line: lines){
            this.processLine(platformNumber,line);
        }
    }

    private void processLine(String platformNumber,String line){
        String urlToApi = "https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238"+
         "&busstopId="+this.stationId+"&busstopNr="+platformNumber+"&line="+line+"&apikey="+Properties.API_KEY;
        System.out.println(urlToApi);
    }

    private Optional<Station> findStation(String proposedName) {
        return Optional.ofNullable(this.stationsMap.get(proposedName));
    }
}