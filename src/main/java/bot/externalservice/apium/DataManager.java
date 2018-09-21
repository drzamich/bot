package bot.externalservice.apium;

import bot.externalservice.apium.data.Station;
import bot.processor.Utilities;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    protected Map<String, Station> stationsMap = new HashMap<>();
    protected final String TIME_PATTERN = "HH:mm:ss";
    protected final String PATH_TO_OBJECTS = "src/main/java/bot/externalservice/apium/data/serialized/";
    public String time;
    public String date;
    public String pathToStationMap;

    public DataManager(){
        this.time = Utilities.getTime(TIME_PATTERN);
        this.date = Utilities.getTime("yyyyMMdd");
        this.pathToStationMap = PATH_TO_OBJECTS+this.date+"_statonMap";

    }

//    protected void serializeStationMap() {
//        StationsMap stationsMapMap = new StationsMap(this.stationsMap);
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(stationMapSerializedUrl);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//            objectOutputStream.writeObject(stationsMap);
//            objectOutputStream.flush();
//            objectOutputStream.close();
//            System.out.println("Station list saved.");
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    protected Map<String,Station> deserializeStationMap(){
//        Map<String,Station> res = new HashMap<>();
//        try {
//            FileInputStream fileInputStream = new FileInputStream(stationMapSerializedUrl);
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            res = (HashMap<String, Station>) objectInputStream.readObject();
//            objectInputStream.close();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return res;
//    }

}
