package bot.externalservice.siptw.response;

import bot.externalservice.siptw.dto.SipTwDepartureDto;
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

    public static SipTwDeparture fromSipTwDepartureDto(SipTwDepartureDto sipTwDepartureDto) {
        return new SipTwDeparture(sipTwDepartureDto.getDestination(), sipTwDepartureDto.getLine(), sipTwDepartureDto.getArrival());
    }
}