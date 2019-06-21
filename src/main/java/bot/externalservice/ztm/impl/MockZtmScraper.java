package bot.externalservice.ztm.impl;

import bot.externalservice.ztm.ZtmScraper;
import bot.externalservice.ztm.response.ZtmDeparture;
import bot.externalservice.ztm.response.ZtmPlatform;
import bot.externalservice.ztm.response.ZtmStation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service("mockZtm")
public class MockZtmScraper implements ZtmScraper {

    @Override
    public List<ZtmStation> getZtmStationList() {

        LocalDateTime time1 = LocalDateTime.now(ZoneId.of("CET")).withHour(4).withMinute(47).withSecond(0).withNano(0).plusDays(0);
        LocalDateTime time2 = LocalDateTime.now(ZoneId.of("CET")).withHour(4).withMinute(8).withSecond(0).withNano(0).plusDays(0);
        LocalDateTime time3 = LocalDateTime.now(ZoneId.of("CET")).withHour(4).withMinute(10).withSecond(0).withNano(0).plusDays(0);

        ZtmPlatform platform01 = new ZtmPlatform("01", "1.Praskiego Pułku", Arrays.asList("411","502","704","720","722","730","N21"),"https://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=2294&o=01",
                Arrays.asList(new ZtmDeparture(time1, "704", "Wiatraczna")));

        ZtmPlatform platform02 = new ZtmPlatform("02", "Nizinna", Arrays.asList("411","502","704","720","722","730"),"https://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=2294&o=02",
                Arrays.asList(new ZtmDeparture(time2, "722", "Radiówek"), new ZtmDeparture(time3, "704", "PKP Halinów")));

        ZtmStation stationOne = new ZtmStation("2294", "Fabryczna (Warszawa)", "https://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=2294", Arrays.asList(platform01, platform02));


        return Collections.singletonList(stationOne);
    }
}
