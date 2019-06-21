package bot.externalservice.siptw.response;

import bot.externalservice.siptw.dto.GetLatestPanelPredictionsResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class SipTwDeparture {

    private String line;

    private String destination;

    private String timeToArrivalInMinutes;

    public static SipTwDeparture fromLatestPanelPredictionsResponse(GetLatestPanelPredictionsResponse getLatestPanelPredictionsResponse) {
        return new SipTwDeparture(getLatestPanelPredictionsResponse.getDestination(), getLatestPanelPredictionsResponse.getLine(), getLatestPanelPredictionsResponse.getArrival());
    }
}