package bot.externalservice.siptw.response;


import bot.externalservice.siptw.dto.GetStopsResponse;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SipTwPlatformsResponse {

    public static final SipTwPlatformsResponse INVALID = new SipTwPlatformsResponse(false, Collections.emptyList());

    private final boolean isValid;

    private final List<SipTwPlatform> sipTwPlatforms;

    public SipTwPlatformsResponse(GetStopsResponse[] getStopsResponse){
        this.isValid = getStopsResponse.length > 0;
        this.sipTwPlatforms = Stream.of(getStopsResponse)
                .map(SipTwPlatform::fromStopsResponse)
                .collect(Collectors.toList());
    }
}
