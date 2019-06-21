package bot.schema.dto;

import bot.externalservice.siptw.response.SipTwDeparture;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DepartureDto {
    private String line;
    private String direction;
    private int timeToDepartureInMinutes;

    public static DepartureDto fromSipTwDeparture(SipTwDeparture sipTwDeparture) {
        return new DepartureDto(sipTwDeparture.getLine(), sipTwDeparture.getDestination(), Integer.valueOf(sipTwDeparture.getTimeToArrivalInMinutes()));
    }
}
