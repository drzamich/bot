package bot.schema;

import bot.utils.StringHelper;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

@Getter
public class StationRequest {

    private static final int MINIMAL_REASONABLE_STATION_LENGTH = 3;

    @Getter(AccessLevel.NONE)
    private boolean containsPlatformNumber;

    private final boolean isValid;

    private Integer platformNumber;

    private String stationName;

    public StationRequest(String requestString) {
        isValid = isValid(requestString);
        if (isValid) {
            String sanitizedInput = StringHelper.sanitizeInput(requestString);
            setUpStationDetails(sanitizedInput);
        }
    }

    private void setUpStationDetails(String sanitizedInput) {
        List<String> queryElements = Arrays.asList(sanitizedInput.split(StringUtils.SPACE));
        String probablePlatformNumber = queryElements.get(queryElements.size() - 1);
        if (StringHelper.isNumeric(probablePlatformNumber)) {
            // TODO edge case'y typu jp2 / stacja i kierunek
            containsPlatformNumber = true;
            platformNumber = Integer.valueOf(probablePlatformNumber);
            stationName = sanitizedInput.replaceAll(sanitizedInput, StringUtils.EMPTY);
        } else {
            stationName = String.join(StringUtils.SPACE, queryElements);
        }
    }

    public boolean isValid(String requestString) {
        return requestString.length() >= MINIMAL_REASONABLE_STATION_LENGTH;
    }

    public boolean containsPlatformNumber() {
        return containsPlatformNumber;
    }
}
