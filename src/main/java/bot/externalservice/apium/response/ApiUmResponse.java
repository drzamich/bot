package bot.externalservice.apium.response;

import bot.externalservice.apium.dto.ApiUmResponseDtoWrapper;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class ApiUmResponse {

    private static final ApiUmResponse FAILED = new ApiUmResponse(false, Collections.emptyList());

    private boolean success;

    private List<ApiUmDeparture> departures = Collections.emptyList();

    public static ApiUmResponse failed() {
        return FAILED;
    }

    public ApiUmResponse(ApiUmResponseDtoWrapper apiUmResponseDtoWrapper, String line) {
        if (apiUmResponseDtoWrapper.isSuccess()) {
            success = true;
            departures = new ArrayList<>();
            for (Map<String, List<Map<String, String>>> map : apiUmResponseDtoWrapper.getData().getResult()) {
                String dest = map.get("values").get(3).get("value");
                String time = map.get("values").get(5).get("value");
                departures.add(new ApiUmDeparture(line, dest, time));
            }
        }
    }

    public ApiUmResponse(boolean success, List<ApiUmDeparture> departures) {
        this.success = success;
        this.departures = departures;
    }
}
