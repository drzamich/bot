package bot.externalservice.ztm;

import bot.externalservice.ztm.impl.ZtmConstants;
import bot.externalservice.ztm.impl.ZtmScraperImpl;
import bot.externalservice.ztm.response.ZtmDeparture;
import bot.externalservice.ztm.response.ZtmPlatform;
import bot.externalservice.ztm.response.ZtmStation;
import bot.service.PlatformService;
import com.google.common.collect.Iterables;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.hamcrest.core.Is.is;

@PrepareForTest(Jsoup.class)
@RunWith(PowerMockRunner.class)
public class ZtmScraperTest {
    private static final String BASE_URL = ZtmConstants.BASE_URL;
    private static final String AGGREGATE_PAGE_URL = ZtmConstants.AGGREGATE_PAGE_URL;
    private static final String AL_WIELKOPOLSKI_DETAIL_PAGE_URL = BASE_URL + "rozklad_nowy.php?c=183&l=1&a=4122";
    private static final String FABRYCZNA_DETAIL_PAGE_URL = BASE_URL + "rozklad_nowy.php?c=183&l=1&a=2294";
    private static final String FABRYKA_POMP_DETAIL_PAGE_URL = BASE_URL + "rozklad_nowy.php?c=183&l=1&a=1092";
    private static final String SW_A_BOBOLI_DETAIL_PAGE_URL = BASE_URL + "rozklad_nowy.php?c=183&l=1&a=3235";


    private static final String AL_WIELKOPOLSKI_PLATFORM_01_URL = BASE_URL + "rozklad_nowy.php?c=182&l=1&n=4122&o=01";
    private static final String AL_WIELKOPOLSKI_PLATFORM_02_URL = BASE_URL + "rozklad_nowy.php?c=182&l=1&n=4122&o=02";
    private static final String FABRYCZNA_PLATFORM_01_URL = BASE_URL + "rozklad_nowy.php?c=182&l=1&n=2294&o=01";
    private static final String FABRYCZNA_PLATFORM_02_URL = BASE_URL + "rozklad_nowy.php?c=182&l=1&n=2294&o=02";
    private static final String FABRYKA_POMP_PLATFORM_01_URL = BASE_URL + "rozklad_nowy.php?c=182&l=1&n=1092&o=01";
    private static final String FABRYKA_POMP_PLATFORM_02_URL = BASE_URL + "rozklad_nowy.php?c=182&l=1&n=1092&o=02";
    private static final String FABRYKA_POMP_PLATFORM_04_URL = BASE_URL + "rozklad_nowy.php?c=182&l=1&n=1092&o=04";
    private static final String SW_A_BOBOLI_PLATFORM_01_URL = BASE_URL + "rozklad_nowy.php?c=182&l=1&n=3235&o=01";

    private static final String TIME_ZONE = "CET";

    @Before
    public void setUp() throws IOException, URISyntaxException {
        Document mockedAggregatePage = fromFile("/ztm/aggregatePage.html");
        Document alWielkopolski = fromFile("/ztm/alWielkopolski.html");
        Document fabryczna = fromFile("/ztm/fabryczna.html");
        Document fabrykaPomp = fromFile("/ztm/fabrykaPomp.html");
        Document swABoboli = fromFile("/ztm/swABoboli.html");
        
        Document alWielkopolskiP01 = fromFile("/ztm/alWielkopolskiP01.html");
        Document alWielkopolskiP02 = fromFile("/ztm/alWielkopolskiP02.html");
        Document fabrycznaP01 = fromFile("/ztm/fabrycznaP01.html");
        Document fabrycznaP02 = fromFile("/ztm/fabrycznaP02.html");
        Document fabrykaPompP01 = fromFile("/ztm/fabrykaPompP01.html");
        Document fabrykaPompP02 = fromFile("/ztm/fabrykaPompP02.html");
        Document fabrykaPompP04 = fromFile("/ztm/fabrykaPompP04.html");
        Document swABoboliP01 = fromFile("/ztm/swABoboliP01.html");

        mockStatic(Jsoup.class);
        PowerMockito.when(Jsoup.parse(eq(new URL(AGGREGATE_PAGE_URL)), anyInt())).thenReturn(mockedAggregatePage);
        PowerMockito.when(Jsoup.parse(eq(new URL(AL_WIELKOPOLSKI_DETAIL_PAGE_URL)), anyInt())).thenReturn(alWielkopolski);
        PowerMockito.when(Jsoup.parse(eq(new URL(FABRYCZNA_DETAIL_PAGE_URL)), anyInt())).thenReturn(fabryczna);
        PowerMockito.when(Jsoup.parse(eq(new URL(FABRYKA_POMP_DETAIL_PAGE_URL)), anyInt())).thenReturn(fabrykaPomp);
        PowerMockito.when(Jsoup.parse(eq(new URL(SW_A_BOBOLI_DETAIL_PAGE_URL)), anyInt())).thenReturn(swABoboli);

        PowerMockito.when(Jsoup.parse(eq(new URL(AL_WIELKOPOLSKI_PLATFORM_01_URL)), anyInt())).thenReturn(alWielkopolskiP01);
        PowerMockito.when(Jsoup.parse(eq(new URL(AL_WIELKOPOLSKI_PLATFORM_02_URL)), anyInt())).thenReturn(alWielkopolskiP02);
        PowerMockito.when(Jsoup.parse(eq(new URL(FABRYCZNA_PLATFORM_01_URL)), anyInt())).thenReturn(fabrycznaP01);
        PowerMockito.when(Jsoup.parse(eq(new URL(FABRYCZNA_PLATFORM_02_URL)), anyInt())).thenReturn(fabrycznaP02);
        PowerMockito.when(Jsoup.parse(eq(new URL(FABRYKA_POMP_PLATFORM_01_URL)), anyInt())).thenReturn(fabrykaPompP01);
        PowerMockito.when(Jsoup.parse(eq(new URL(FABRYKA_POMP_PLATFORM_02_URL)), anyInt())).thenReturn(fabrykaPompP02);
        PowerMockito.when(Jsoup.parse(eq(new URL(FABRYKA_POMP_PLATFORM_04_URL)), anyInt())).thenReturn(fabrykaPompP04);
        PowerMockito.when(Jsoup.parse(eq(new URL(SW_A_BOBOLI_PLATFORM_01_URL)), anyInt())).thenReturn(swABoboliP01);
    }

    private Document fromFile(String s) throws IOException, URISyntaxException {
        return Jsoup.parse(new String(Files.readAllBytes(Paths.get(getClass().getResource(s).toURI()))));
    }

    @Test
    public void whenZtmScraperIsInvoked_allWarsawStationsInfoIsGathered() {
        ZtmScraper ztmScraper = new ZtmScraperImpl(new PlatformService());
        List<ZtmStation> ztmStationList = ztmScraper.getZtmStationList();
        assertThat(ztmStationList.size(), equalTo(4));
    }

    @Test
    public void whenZtmScraperIsInvoked_stationDetailsAreFilledCorrectly() {
        ZtmScraper ztmScraper = new ZtmScraperImpl(new PlatformService());
        List<ZtmStation> ztmStationList = ztmScraper.getZtmStationList();
        LocalDateTime time1 = LocalDateTime.now(ZoneId.of(TIME_ZONE)).withHour(4).withMinute(47).withSecond(0).withNano(0).plusDays(0);
        LocalDateTime time2 = LocalDateTime.now(ZoneId.of(TIME_ZONE)).withHour(4).withMinute(8).withSecond(0).withNano(0).plusDays(0);
        LocalDateTime time3 = LocalDateTime.now(ZoneId.of(TIME_ZONE)).withHour(4).withMinute(10).withSecond(0).withNano(0).plusDays(0);

        ZtmPlatform platform01 = new ZtmPlatform("01", "1.Praskiego Pułku", Arrays.asList("411","502","704","720","722","730","N21"),"https://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=2294&o=01",
                Arrays.asList(new ZtmDeparture(time1, "704", "Wiatraczna")));

        ZtmPlatform platform02 = new ZtmPlatform("02", "Nizinna", Arrays.asList("411","502","704","720","722","730"),"https://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=2294&o=02",
                Arrays.asList(new ZtmDeparture(time2, "722", "Radiówek"), new ZtmDeparture(time3, "704", "PKP Halinów")));

        ZtmStation expected = new ZtmStation("2294", "Fabryczna (Warszawa)", "https://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=2294", Arrays.asList(platform01, platform02));
        assertThat(expected, equalTo(ztmStationList.get(1)));
    }

    @Test
    public void whenZtmScraperIsInvokedWithExcludedStationId_allWarsawStationsInfoExceptExcludedIsGathered() {
        PlatformService platformService = PowerMockito.mock(PlatformService.class);
        PowerMockito.when(platformService.getExcludedStationIDs()).thenReturn(Collections.singletonList("4122"));
        ZtmScraper ztmScraper = new ZtmScraperImpl(platformService);
        List<ZtmStation> ztmStationList = ztmScraper.getZtmStationList();
        assertThat(ztmStationList.size(), equalTo(3));
        assertThat(ztmStationList.stream().noneMatch(ztmStation -> ztmStation.getMainName().equals("Al.Wielkopolski (Warszawa)")), is(true));
    }

    @Test
    public void whenZtmScraperIsInvokedWithExcludedPlatformName_stationDetailsAreFilledCorrectlyExceptExcludedPlatform() {
        PlatformService platformService = PowerMockito.mock(PlatformService.class);
        PowerMockito.when(platformService.getExcludedPlatformNames()).thenReturn(Collections.singletonList("Fabryczna (Warszawa) 02"));
        ZtmScraper ztmScraper = new ZtmScraperImpl(platformService);
        List<ZtmStation> ztmStationList = ztmScraper.getZtmStationList();
        LocalDateTime time1 = LocalDateTime.now(ZoneId.of(TIME_ZONE)).withHour(4).withMinute(47).withSecond(0).withNano(0).plusDays(0);

        ZtmPlatform platform01 = new ZtmPlatform("01", "1.Praskiego Pułku", Arrays.asList("411","502","704","720","722","730","N21"),"https://www.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&n=2294&o=01",
                Arrays.asList(new ZtmDeparture(time1, "704", "Wiatraczna")));


        ZtmStation expected = new ZtmStation("2294", "Fabryczna (Warszawa)", "https://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=2294", Collections.singletonList(platform01));
        assertThat(expected, equalTo(ztmStationList.get(1)));
    }

    @Test
    public void whenZtmScraperIsInvoked_DepartureListHasProperLength() {
        ZtmScraper ztmScraper = new ZtmScraperImpl(new PlatformService());
        List<ZtmStation> ztmStationList = ztmScraper.getZtmStationList();

        assertEquals(78, ztmStationList.get(2).getPlatforms().get(0).getDepartures().size());
    }

    @Test
    public void whenZtmScraperIsInvokedWithTimetableForPlatformSpanningOver24Hours_DepartureTimesAreCorrect01() {
        ZtmScraper ztmScraper = new ZtmScraperImpl(new PlatformService());
        List<ZtmStation> ztmStationList = ztmScraper.getZtmStationList();

        LocalDateTime expected = LocalDateTime.now(ZoneId.of(TIME_ZONE)).withHour(3).withMinute(57).withSecond(0).withNano(0).plusDays(1);
        LocalDateTime output = Iterables.getLast(ztmStationList.get(2).getPlatforms().get(0).getDepartures()).getTime();

        assertEquals(expected, output);
    }
}