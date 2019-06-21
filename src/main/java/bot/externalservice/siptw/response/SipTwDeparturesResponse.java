package bot.externalservice.siptw.response;

import bot.externalservice.siptw.dto.GetLatestPanelPredictionsResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SipTwDeparturesResponse {

    public static final SipTwDeparturesResponse INVALID = new SipTwDeparturesResponse(false, Collections.emptyList());

    private final boolean isValid;

    private final List<SipTwDeparture> departures;

    public SipTwDeparturesResponse(GetLatestPanelPredictionsResponse[] getLatestPanelPredictionsResponses) {
        isValid = getLatestPanelPredictionsResponses.length > 0;
        departures = Stream.of(getLatestPanelPredictionsResponses)
                .map(SipTwDeparture::fromLatestPanelPredictionsResponse)
                .collect(Collectors.toList());
    }
}
