package bot.externalservice.siptw.response;

import bot.externalservice.siptw.schema.DepartureSipTw;
import bot.schema.Departure;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@ToString
@EqualsAndHashCode
public class SipServiceDepartureResponse {

    private final List<Departure> departures;

    public SipServiceDepartureResponse(DepartureSipTw[] sipTwDepartures) {
        departures = Stream.of(sipTwDepartures)
                .map(Departure::fromSipTwDeparture)
                .collect(Collectors.toList());
    }
}
