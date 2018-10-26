package bot.processor;

import bot.schema.Station;

import java.util.Map;

public class Settings {
    protected String date = Utilities.getTime("yyyyMMdd");
    protected final String PATH_SERIALIZATION = "src/main/java/bot/data/serialized/"+date;
    protected final String PATH_SERIALIZATION_NO_DATE = "src/main/java/bot/data/serialized/";
    protected final String PATH_LIST_ZTM = PATH_SERIALIZATION_NO_DATE+"00_ztm_list";
    protected final String PATH_MAP_SIPTW = PATH_SERIALIZATION_NO_DATE+"00_siptw_map";
    protected final String PATH_TO_STATION_MAP = PATH_SERIALIZATION + "_station_map";

    protected Map<String, Station> stationMap;
}
