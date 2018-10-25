package bot.processor;

import bot.externalservice.apium.data.Station;

import java.util.Map;

public class Settings {
    protected String date = Utilities.getTime("yyyyMMdd");
    protected final String PATH_SERIALIZATION = "src/main/java/bot/data/serialized/"+date;
    protected final String PATH_TO_STATION_MAP = PATH_SERIALIZATION + "_station_map";

    protected Map<String, Station> stationMap;
}
