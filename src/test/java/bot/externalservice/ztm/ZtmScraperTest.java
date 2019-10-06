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
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;

@PrepareForTest(Jsoup.class)
@RunWith(PowerMockRunner.class)
public class ZtmScraperTest {
    private static String getStationUrl(String stationId) {
        return ZtmConstants.BASE_URL + "?wtp_md=4&wtp_dt=2019-10-06&wtp_dy=7&wtp_st=" + stationId;
    }

    private static String getPlatformUrl(String stationId, String platformNumber) {
        return ZtmConstants.BASE_URL + "?wtp_md=7&wtp_dt=2019-10-06&wtp_st=" + stationId + "&wtp_dy=7&wtp_pt=" + platformNumber;
    }

    private static final String AL_WIELKOPOLSKI_DETAIL_PAGE_URL = getStationUrl("4122");
    private static final String FABRYCZNA_DETAIL_PAGE_URL = getStationUrl("2294");
    private static final String FABRYKA_POMP_DETAIL_PAGE_URL = getStationUrl("1092");
    private static final String SW_A_BOBOLI_DETAIL_PAGE_URL = getStationUrl("3235");


    private static final String AL_WIELKOPOLSKI_PLATFORM_01_URL = getPlatformUrl("4122", "01");
    private static final String AL_WIELKOPOLSKI_PLATFORM_02_URL = getPlatformUrl("4122", "02");
    private static final String FABRYCZNA_PLATFORM_01_URL = getPlatformUrl("2294", "01");
    private static final String FABRYCZNA_PLATFORM_02_URL = getPlatformUrl("2294", "02");
    private static final String FABRYKA_POMP_PLATFORM_01_URL = getPlatformUrl("1092", "01");;
    private static final String FABRYKA_POMP_PLATFORM_02_URL = getPlatformUrl("1092", "02");;
    private static final String FABRYKA_POMP_PLATFORM_04_URL = getPlatformUrl("1092", "04");;
    private static final String SW_A_BOBOLI_PLATFORM_01_URL = getPlatformUrl("3235", "01");;

    @Before
    public void setUp() throws IOException, URISyntaxException {
        Document mockedAggregatePage = fromFile("/ztm/aggregatePage.htm");
        Document alWielkopolski = fromFile("/ztm/alWielkopolski.htm");
        Document fabryczna = fromFile("/ztm/fabryczna.htm");
        Document fabrykaPomp = fromFile("/ztm/fabrykaPomp.htm");
        Document swABoboli = fromFile("/ztm/swABoboli.htm");
        
        Document alWielkopolskiP01 = fromFile("/ztm/alWielkopolskiP01.htm");
        Document alWielkopolskiP02 = fromFile("/ztm/alWielkopolskiP02.htm");
        Document fabrycznaP01 = fromFile("/ztm/fabrycznaP01.htm");
        Document fabrycznaP02 = fromFile("/ztm/fabrycznaP02.htm");
        Document fabrykaPompP01 = fromFile("/ztm/fabrykaPompP01.htm");
        Document fabrykaPompP02 = fromFile("/ztm/fabrykaPompP02.htm");
        Document fabrykaPompP04 = fromFile("/ztm/fabrykaPompP04.htm");
        Document swABoboliP01 = fromFile("/ztm/swABoboliP01.htm");

        PowerMockito.mockStatic(Jsoup.class);
        PowerMockito.when(Jsoup.parse(eq(new URL(ZtmConstants.AGGREGATE_PAGE_URL)), anyInt())).thenReturn(mockedAggregatePage);
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
        LocalDateTime departureTime;
        ZtmDeparture departure;
        ZtmPlatform platform;
        List<String> lines;
        String mainDirection;

        ZtmStation generatedStation = ztmStationList.get(1);

        platform = generatedStation.getPlatforms().get(0);
        lines = Arrays.asList("411","502","704","720","722","730","N21");
        assertThat(lines, equalTo(platform.getLines()));

        mainDirection = "1.Praskiego Pułku";
        assertEquals(mainDirection, platform.getMainDirection());

        departureTime = LocalDateTime.now(ZoneId.of(ZtmConstants.TIME_ZONE)).withHour(4).withMinute(47).withSecond(0).withNano(0).plusDays(0);
        departure = new ZtmDeparture(departureTime, "704", "Wiatraczna");
        assertEquals(departure, platform.getDepartures().get(0));


        platform = generatedStation.getPlatforms().get(1);
        lines = Arrays.asList("411","502","704","720","722","730");
        assertThat(lines, equalTo(platform.getLines()));

        mainDirection = "Nizinna";
        assertEquals(mainDirection, platform.getMainDirection());

        departureTime = LocalDateTime.now(ZoneId.of(ZtmConstants.TIME_ZONE)).withHour(4).withMinute(3).withSecond(0).withNano(0).plusDays(0);
        departure = new ZtmDeparture(departureTime, "722", "Radiówek");
        assertEquals(departure, platform.getDepartures().get(0));

        departureTime = LocalDateTime.now(ZoneId.of(ZtmConstants.TIME_ZONE)).withHour(4).withMinute(10).withSecond(0).withNano(0).plusDays(0);
        departure = new ZtmDeparture(departureTime, "704", "PKP Halinów");
        assertEquals(departure, platform.getDepartures().get(01));
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
        PowerMockito.when(platformService.getExcludedPlatformNames()).thenReturn(Collections.singletonList("Fabryczna 02"));
        ZtmScraper ztmScraper = new ZtmScraperImpl(platformService);
        List<ZtmStation> ztmStationList = ztmScraper.getZtmStationList();

        assertThat(1, equalTo(ztmStationList.get(1).getPlatforms().size()));
    }

    @Test
    public void whenZtmScraperIsInvoked_DepartureListHasProperLength() {
        ZtmScraper ztmScraper = new ZtmScraperImpl(new PlatformService());
        List<ZtmStation> ztmStationList = ztmScraper.getZtmStationList();

        assertEquals(77, ztmStationList.get(2).getPlatforms().get(0).getDepartures().size());
    }

    @Test
    public void whenZtmScraperIsInvokedWithTimetableForPlatformSpanningOver24Hours_DepartureTimesAreCorrect01() {
        ZtmScraper ztmScraper = new ZtmScraperImpl(new PlatformService());
        List<ZtmStation> ztmStationList = ztmScraper.getZtmStationList();

        LocalDateTime expected = LocalDateTime.now(ZoneId.of(ZtmConstants.TIME_ZONE)).withHour(3).withMinute(57).withSecond(0).withNano(0).plusDays(1);
        LocalDateTime output = Iterables.getLast(ztmStationList.get(2).getPlatforms().get(0).getDepartures()).getTime();

        assertEquals(expected, output);
    }
}