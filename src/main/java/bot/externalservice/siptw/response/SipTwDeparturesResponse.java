package bot.externalservice.siptw.response;

import bot.externalservice.siptw.dto.SipTwDepartureDto;
import bot.schema.Departure;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
