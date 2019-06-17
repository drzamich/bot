package bot.externalservice.ztm;

import bot.externalservice.ztm.response.ZtmStation;

import java.util.List;

public interface ZtmScraper {

    List<ZtmStation> getZtmStationList();
}
