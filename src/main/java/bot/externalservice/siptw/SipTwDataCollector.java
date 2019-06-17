package bot.externalservice.siptw;

import bot.externalservice.siptw.response.SipTwPlatform;

import java.util.Map;

public interface SipTwDataCollector {
    Map<String, SipTwPlatform> fetchPlatformMap();
}
