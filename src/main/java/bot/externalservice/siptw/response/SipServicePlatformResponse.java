package bot.externalservice.siptw.response;


import bot.externalservice.siptw.schema.PlatformSipTw;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@Getter
@ToString
public class SipServicePlatformResponse {

    private List<PlatformSipTw> platformSipTws;

    public SipServicePlatformResponse(PlatformSipTw[] platformSipTws){
        this.platformSipTws = Arrays.asList(platformSipTws);
    }
}
