package bot.externalservice.siptw;

import bot.externalservice.siptw.response.SipTwDeparturesResponse;
import bot.externalservice.siptw.response.SipTwPlatformsResponse;

public interface SipService {

    SipTwDeparturesResponse getTimetableForPlatform(int platformID);

    SipTwPlatformsResponse getPlatforms();
}
