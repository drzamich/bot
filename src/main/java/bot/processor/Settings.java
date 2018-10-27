package bot.processor;

import bot.schema.Station;

import java.util.Map;

public class Settings {
    protected String date = Utilities.getTime("yyyyMMdd");
    protected final String MAIN_PATH = "src/main/java/bot/data/";
//    protected final String PATH_SERIALIZATION = MAIN_PATH+"serialized/"+date;
    protected final String PATH_SERIALIZATION = "src/main/java/bot/data/serialized/";
    protected final String PATH_LIST_ZTM = PATH_SERIALIZATION +"00_ztm_list";
    protected final String PATH_MAP_SIPTW = PATH_SERIALIZATION +"00_siptw_map";
    protected final String PATH_TO_STATION_MAP = PATH_SERIALIZATION + "_station_map";
    protected final String PATH_SAVE_PLATFORMS_RAW = MAIN_PATH+"00_platforms_raw.xlsx";
    protected final String PATH_SAVE_STATIONS_RAw = MAIN_PATH+"/00_stations_raw.xlsx";
    protected final String PATH_LOAD_PLATFORM_CHANGES = MAIN_PATH+"00_platforms_extras.xlxs";
    protected final String PATH_LOAD_STATION_CHANGES = MAIN_PATH+"00_stations_extras.xlxs";

    protected Map<String, Station> stationMap;
}
