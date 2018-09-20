package bot.externalservice.apium.data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private String stationMapSerializedUrl = "src/main/java/bot/externalservice/apium/data/stationsMap.txt";
    protected Map<String,Station> stationsMap = new HashMap<>();

    protected void serializeStationMap() {
        StationsMap stationsMapMap = new StationsMap(this.stationsMap);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(stationMapSerializedUrl);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(stationsMap);
            objectOutputStream.flush();
            objectOutputStream.close();
            System.out.println("Station list saved.");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected Map<String,Station> deserializeStationMap(){
        Map<String,Station> res = new HashMap<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(stationMapSerializedUrl);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            res = (HashMap<String, Station>) objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

}
