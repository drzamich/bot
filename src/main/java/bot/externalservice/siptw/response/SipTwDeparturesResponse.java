package bot.externalservice.siptw.response;

import bot.externalservice.siptw.dto.SipTwDepartureDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
@Getter
public class SipTwDeparturesResponse {

    private final List<SipTwDeparture> departures;

    public SipTwDeparturesResponse(SipTwDepartureDto[] sipTwDepartureDtos) {
        departures = Stream.of(sipTwDepartureDtos)
                .map(SipTwDeparture::fromSipTwDepartureDto)
                .collect(Collectors.toList());
    }
}
