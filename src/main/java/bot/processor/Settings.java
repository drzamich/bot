package bot.processor;

import bot.schema.Station;

import java.util.Map;

public class Settings {
    protected String date = Utilities.getTime("yyyyMMdd");
    protected final String USER_HOME = System.getProperty("user.home");
    protected final String MAIN_PATH = USER_HOME+ "/Java/bot/data";
    protected final String PATH_SERIALIZATION = MAIN_PATH + "/serialized/";

    protected final String PATH_LIST_ZTM = PATH_SERIALIZATION + "00_ztm_list";
    protected final String PATH_MAP_SIPTW = PATH_SERIALIZATION + "00_siptw_map";
    protected final String PATH_SAVE_PLATFORMS_RAW = MAIN_PATH + "00_platforms_raw.xlsx";
    protected final String PATH_SAVE_STATIONS_RAw = MAIN_PATH + "00_stations_raw.xlsx";
    protected final String PATH_DIRECTIONS_CUSTOM = MAIN_PATH + "00_directions.xlsx";
    protected final String PATH_ACCEPTED_NAMES_CUSTOM = MAIN_PATH + "00_accepted_names.xlsx";
    protected final String PATH_INTEGRATED_LIST = PATH_SERIALIZATION + "00_integrated_list";
    protected final String PATH_FINAL_LIST = PATH_SERIALIZATION + "00_final_list";
    protected final String PATH_FINAL_MAP = PATH_SERIALIZATION + "00_final_map";
}
