package bot.externalservice.apium;

import bot.externalservice.apium.data.DataManager;
import bot.externalservice.apium.data.Station;
import bot.processor.Utilities;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class StationService extends DataManager {
    private Station station;

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
        System.out.println(this.station);
    }

    private Optional<Station> findStation(String proposedName) {
        return Optional.ofNullable(this.stationsMap.get(proposedName));
    }
}