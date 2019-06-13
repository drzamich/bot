package bot.externalservice.ztm.impl;

import bot.externalservice.ztm.ZtmScraper;
import bot.externalservice.ztm.response.ZtmPlatform;
import bot.externalservice.ztm.response.ZtmStation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service("mockZtm")
public class MockZtmScraper implements ZtmScraper {

    @Override
    public List<ZtmStation> getZtmStationList() {
        ZtmStation stationOne = new ZtmStation("1092", "Fabryka Pomp (Warszawa)", "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=1092");
        stationOne.setPlatforms(Arrays.asList(
                new ZtmPlatform("01", "Chłodnia", Collections.singletonList("Chłodnia"), Arrays.asList("126", "176", "N64")),
                new ZtmPlatform("02", "Daniszewska", Collections.singletonList("Daniszewska"), Arrays.asList("126", "176", "214", "N64")),
                new ZtmPlatform("04", "Faradaya", Collections.singletonList("Faradaya"), Collections.singletonList("214"))
        ));
        ZtmStation stationTwo = new ZtmStation("4122", "Al.Wielkopolski (Warszawa)", "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=4122");
        stationTwo.setPlatforms(Arrays.asList(
                new ZtmPlatform("01", "GUS", Collections.singletonList("GUS"), Arrays.asList("167", "182", "187", "188")),
                new ZtmPlatform("02", "Pomnik Lotnika", Collections.singletonList("Pomnik Lotnika"), Arrays.asList("167", "182", "187", "188"))
        ));
        return Arrays.asList(stationOne, stationTwo);
    }
}
