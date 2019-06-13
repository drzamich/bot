package bot.externalservice.ztm;

import bot.externalservice.ztm.impl.ZtmScraperImpl;
import bot.externalservice.ztm.response.ZtmPlatform;
import bot.externalservice.ztm.response.ZtmStation;
import bot.service.PlatformService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.hamcrest.core.Is.is;

@PrepareForTest(Jsoup.class)
@RunWith(PowerMockRunner.class)
public class ZtmScraperTest {

    private static final String AGGREGATE_PAGE_URL = "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1";
    private static final String AL_WIELKOPOLSKI_DETAIL_PAGE_URL = "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=4122";
    private static final String FABRYCZNA_DETAIL_PAGE_URL = "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=2294";
    private static final String FABRYKA_POMP_DETAIL_PAGE_URL = "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=1092";
    private static final String SW_A_BOBOLI_DETAIL_PAGE_URL = "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=3235";

    @Before
    public void setUp() throws IOException, URISyntaxException {
        Document mockedAggregatePage = fromFile("/ztm/aggregatePage.html");
        Document alWielkopolski = fromFile("/ztm/alWielkopolski.html");
        Document fabryczna = fromFile("/ztm/fabryczna.html");
        Document fabrykaPomp = fromFile("/ztm/fabrykaPomp.html");
        Document swABoboli = fromFile("/ztm/swABoboli.html");

        mockStatic(Jsoup.class);
        PowerMockito.when(Jsoup.parse(eq(new URL(AGGREGATE_PAGE_URL)), anyInt())).thenReturn(mockedAggregatePage);
        PowerMockito.when(Jsoup.parse(eq(new URL(AL_WIELKOPOLSKI_DETAIL_PAGE_URL)), anyInt())).thenReturn(alWielkopolski);
        PowerMockito.when(Jsoup.parse(eq(new URL(FABRYCZNA_DETAIL_PAGE_URL)), anyInt())).thenReturn(fabryczna);
        PowerMockito.when(Jsoup.parse(eq(new URL(FABRYKA_POMP_DETAIL_PAGE_URL)), anyInt())).thenReturn(fabrykaPomp);
        PowerMockito.when(Jsoup.parse(eq(new URL(SW_A_BOBOLI_DETAIL_PAGE_URL)), anyInt())).thenReturn(swABoboli);
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
        ZtmStation expected = new ZtmStation("1092", "Fabryka Pomp (Warszawa)", "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=1092");
        expected.setPlatforms(Arrays.asList(
                new ZtmPlatform("01", "Chłodnia", Collections.singletonList("Chłodnia"), Arrays.asList("126", "176", "N64")),
                new ZtmPlatform("02", "Daniszewska", Collections.singletonList("Daniszewska"), Arrays.asList("126", "176", "214", "N64")),
                new ZtmPlatform("04", "Faradaya", Collections.singletonList("Faradaya"), Collections.singletonList("214"))
        ));
        assertThat(expected, equalTo(ztmStationList.get(2)));
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
        PowerMockito.when(platformService.getExcludedPlatformNames()).thenReturn(Collections.singletonList("Fabryka Pomp (Warszawa) 02"));
        ZtmScraper ztmScraper = new ZtmScraperImpl(platformService);
        List<ZtmStation> ztmStationList = ztmScraper.getZtmStationList();
        ZtmStation expected = new ZtmStation("1092", "Fabryka Pomp (Warszawa)", "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=1092");
        expected.setPlatforms(Arrays.asList(
                new ZtmPlatform("01", "Chłodnia", Collections.singletonList("Chłodnia"), Arrays.asList("126", "176", "N64")),
                new ZtmPlatform("04", "Faradaya", Collections.singletonList("Faradaya"), Collections.singletonList("214"))
        ));
        assertThat(expected, equalTo(ztmStationList.get(2)));
    }
}