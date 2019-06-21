package bot.externalservice.siptw.response;

import bot.externalservice.siptw.dto.GetStopsResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class SipTwPlatform {

    private int platformId;

    private String platformName;

    private String direction;

    public static SipTwPlatform fromStopsResponse(GetStopsResponse getStopsResponse) {
        return new SipTwPlatform(getStopsResponse.getId(), getStopsResponse.getName(), getStopsResponse.getDirection());
    }
}
