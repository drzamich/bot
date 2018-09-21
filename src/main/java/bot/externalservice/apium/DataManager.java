package bot.externalservice.apium;

import bot.externalservice.apium.data.Station;
import bot.processor.Utilities;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    protected Map<String, Station> stationsMap = new HashMap<>();
    protected final String TIME_PATTERN = "HH:mm:ss";
    protected final String PATH_TO_OBJECTS = "src/main/java/bot/externalservice/apium/data/serialized/";
    public String time;
    public String date;
    public String pathToStationMap;
    public String pathToStationList;
    public List<String> BLOCKED_STOPS = Arrays.asList("Wyścigi 08","Pl.Małachowskiego 02","Metro Młociny 20","Tor Służewiec 01");

    public DataManager(){
        this.time = Utilities.getTime(TIME_PATTERN);
        this.date = Utilities.getTime("yyyyMMdd");
        this.pathToStationMap = PATH_TO_OBJECTS+this.date+"_statonMap";
        this.pathToStationList = PATH_TO_OBJECTS+this.date+"_stationList";

    }


}
