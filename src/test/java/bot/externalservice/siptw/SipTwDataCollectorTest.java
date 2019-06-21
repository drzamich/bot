package bot.externalservice.siptw;

import bot.externalservice.siptw.dto.GetStopsResponse;
import bot.externalservice.siptw.impl.SipTwDataCollectorImpl;
import bot.externalservice.siptw.response.SipTwPlatform;
import bot.externalservice.siptw.response.SipTwPlatformsResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SipTwDataCollectorTest {

    @Mock
    private SipService sipService;

    private SipTwDataCollector sipTwDataCollector;

    @Before
    public void setUp() {
        Mockito.when(sipService.getPlatforms()).thenReturn(mockExternalServicePlatformsResponse());
        sipTwDataCollector = new SipTwDataCollectorImpl(sipService);
    }

    @Test
    public void testParsedPlatformsStructure() {
        Map<String, SipTwPlatform> expectedPlatforms = new TreeMap<>();
        expectedPlatforms.put("KIJOWSKA 03", new SipTwPlatform(100103, "KIJOWSKA [03]", "KINO FEMINA"));
        expectedPlatforms.put("KIJOWSKA 04", new SipTwPlatform(100104, "KIJOWSKA [04]", "ZĄBKOWSKA"));
        expectedPlatforms.put("DWORZEC WILEŃSKI 03", new SipTwPlatform(100303, "DWORZEC WILEŃSKI [03]", "PARK PRASKI"));
        assertEquals(expectedPlatforms, sipTwDataCollector.fetchPlatformMap());
    }

    private SipTwPlatformsResponse mockExternalServicePlatformsResponse() {
        GetStopsResponse getStopsResponse = new GetStopsResponse();
        getStopsResponse.setId(100103);
        getStopsResponse.setName("KIJOWSKA [03]");
        getStopsResponse.setDirection("KINO FEMINA");

        GetStopsResponse getStopsResponse2 = new GetStopsResponse();
        getStopsResponse2.setId(100104);
        getStopsResponse2.setName("KIJOWSKA [04]");
        getStopsResponse2.setDirection("ZĄBKOWSKA");

        GetStopsResponse getStopsResponse3 = new GetStopsResponse();
        getStopsResponse3.setId(100303);
        getStopsResponse3.setName("DWORZEC WILEŃSKI [03]");
        getStopsResponse3.setDirection("PARK PRASKI");

        return new SipTwPlatformsResponse(new GetStopsResponse[]{getStopsResponse, getStopsResponse2, getStopsResponse3});
    }
}