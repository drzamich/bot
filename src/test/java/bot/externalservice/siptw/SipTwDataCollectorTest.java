package bot.externalservice.siptw;

import bot.externalservice.siptw.dto.SipTwPlatformDto;
import bot.externalservice.siptw.impl.SipServiceImpl;
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
        SipTwPlatformDto sipTwPlatformDto = new SipTwPlatformDto();
        sipTwPlatformDto.setId(100103);
        sipTwPlatformDto.setName("KIJOWSKA [03]");
        sipTwPlatformDto.setDirection("KINO FEMINA");

        SipTwPlatformDto sipTwPlatformDto2 = new SipTwPlatformDto();
        sipTwPlatformDto2.setId(100104);
        sipTwPlatformDto2.setName("KIJOWSKA [04]");
        sipTwPlatformDto2.setDirection("ZĄBKOWSKA");

        SipTwPlatformDto sipTwPlatformDto3 = new SipTwPlatformDto();
        sipTwPlatformDto3.setId(100303);
        sipTwPlatformDto3.setName("DWORZEC WILEŃSKI [03]");
        sipTwPlatformDto3.setDirection("PARK PRASKI");

        return new SipTwPlatformsResponse(new SipTwPlatformDto[]{sipTwPlatformDto, sipTwPlatformDto2, sipTwPlatformDto3});
    }
}