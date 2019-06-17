package bot.externalservice.siptw.response;


import bot.externalservice.siptw.dto.SipTwPlatformDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
@Getter
@ToString
public class SipTwPlatformsResponse {

    private List<SipTwPlatform> sipTwPlatforms;

    public SipTwPlatformsResponse(SipTwPlatformDto[] sipTwPlatformDtos){
        this.sipTwPlatforms = Stream.of(sipTwPlatformDtos)
                .map(SipTwPlatform::fromSipTwPlatformDto)
                .collect(Collectors.toList());
    }
}
