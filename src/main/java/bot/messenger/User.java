package bot.messenger;

import bot.Settings;
import bot.utils.FileHelper;
import bot.utils.StringHelper;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userID;
    private Map<String, Integer> requestMap = new HashMap<>();
    private String date;
    private String pathToRequestMap;

    public User(String userID) {
        this.userID = userID;
        this.date = StringHelper.getTime(Settings.DATE_PATTERN);
        this.pathToRequestMap = Settings.MESSENGER_USERS_REQUESTS_PATH + this.userID;
    }

    /**
     * Validates if user has exceeded the maximal number of requests per day and allows the messenger callback handler
     * to act accoringly by returning a specific integer. If the number of requests is below or equal the maximal value,
     * it returns zero. If the number of requests is above the maximal number but within the boundry set by the
     * numberOfWarnings variable, returns 1. If the number of requests exceeds all that, returns 2.
     * <p>
     * This allows minimizing number of actions for the engine to take when the flood attempt is being made.
     * In such case the requestMap will not be saved and the error messsage will not be sent to the user.
     *
     * @return Integer indicating what action is to be undertaken by the messenger callback handler.
     */
    public int checkFloodAttempt(Integer maxRequestsPerDay, Integer numberOfWarnings) {
        int requests = checkUserRequests();
        if (requests <= maxRequestsPerDay + numberOfWarnings) {
            this.requestMap.put(date, requests + 1);
            saveRequestMap();
        }
        if (requests <= maxRequestsPerDay || Settings.privilegedUsers.contains(this.userID)) {
            return 0;
        }
        if (requests <= maxRequestsPerDay+numberOfWarnings) {
            return 1;
        }
        return 2;
    }

    private int checkUserRequests() {
        loadRequestMap();
        return this.requestMap.getOrDefault(date, 0);
    }

    private void loadRequestMap() {
        if (FileHelper.fileExists(pathToRequestMap)) {
            this.requestMap = FileHelper.deserializeObject(pathToRequestMap);
        }
    }

    private void saveRequestMap() {
        FileHelper.serializeObject(this.requestMap,this.pathToRequestMap);
    }
}
