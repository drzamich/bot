package bot;

import java.util.Arrays;
import java.util.List;

public class Settings {
        public static final String BASE_FOLDER_NAME = "bot";

        public static final String TIME_PATTERN = "HH:mm:ss";
        public static final String DATE_PATTERN = "yyyyMMdd";

        public static final String USER_HOME = System.getProperty("user.home");
        public static final String MAIN_DATA_PATH = USER_HOME + "/IdeaProjects/" + Settings.BASE_FOLDER_NAME + "/data/";
        public static final String PATH_SERIALIZATION = MAIN_DATA_PATH + "serialized/";
        public static final String PATH_SAVED_TIMETABLES = PATH_SERIALIZATION + "apium/";
        public static final String MESSENGER_DATA_PATH = MAIN_DATA_PATH + "messenger/";
        public static final String MESSENGER_USERS_PATH = MESSENGER_DATA_PATH +"users/";
        public static final String MESSENGER_USERS_REQUESTS_PATH = MESSENGER_USERS_PATH +"requests/";

        public static final String PATH_EXCEL_PLATFORMS_RAW = MAIN_DATA_PATH + "00_platforms_raw.xlsx";
        public static final String PATH_EXCEL_STATIONS_RAw = MAIN_DATA_PATH + "00_stations_raw.xlsx";
        public static final String PATH_DIRECTIONS_CUSTOM = MAIN_DATA_PATH + "00_directions.xlsx";
        public static final String PATH_ACCEPTED_NAMES_CUSTOM = MAIN_DATA_PATH + "00_accepted_names.xlsx";

        public static final String PATH_LIST_ZTM = PATH_SERIALIZATION + "00_ztm_list";
        public static final String PATH_MAP_SIPTW = PATH_SERIALIZATION + "00_siptw_map";
        public static final String PATH_FINAL_LIST = PATH_SERIALIZATION + "00_final_list";
        public static final String PATH_FINAL_MAP = PATH_SERIALIZATION + "00_final_map";
        public static final String PATH_INTEGRATED_LIST = PATH_SERIALIZATION + "00_integrated_list";

        public static final String APIUM_API_KEY = "e476edb4-e08c-4adc-ade1-c9b22ce99c64";

        public static final List<String> privilegedUsers = Arrays.asList("2478097495551575");
}