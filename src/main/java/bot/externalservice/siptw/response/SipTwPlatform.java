package bot.externalservice.siptw.response;

import bot.externalservice.siptw.dto.SipTwPlatformDto;
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

    public static SipTwPlatform fromSipTwPlatformDto(SipTwPlatformDto sipTwPlatformDto) {
        return new SipTwPlatform(sipTwPlatformDto.getId(), sipTwPlatformDto.getName(), sipTwPlatformDto.getDirection());
    }
}
