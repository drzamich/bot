package bot.externalservice.siptw;

import bot.externalservice.siptw.configuration.SipTwConfiguration;
import bot.externalservice.siptw.response.SipTwDeparture;
import bot.externalservice.siptw.response.SipTwPlatform;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestTemplate.class, SipService.class, SipTwConfiguration.class})
public class SipTwServiceTest {

    @Autowired
    private SipService sipService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testSipServiceDeparturesResponse() throws IOException, URISyntaxException {
        String json = fromFile("/siptw/departures.json");
        mockServer.expect(MockRestRequestMatchers.anything()).
                andRespond(MockRestResponseCreators.withSuccess(json, MediaType.APPLICATION_JSON)); //TODO przerobić na MediaType.TEXT_HTML tak jak naprawdę zwraca serwis. ale jest jakiś problem że customowy konwerter się nie rejestruje w teście
        assertThat(sipService.getTimetableForPlatform(323904).getDepartures()).
                isEqualTo(mockExternalServiceDeparturesResponseList());
    }

    @Test
    public void testSipServicePlatformsResponse() throws IOException, URISyntaxException {
        String json = fromFile("/siptw/platforms.json");
        mockServer.expect(MockRestRequestMatchers.anything()).
                andRespond(MockRestResponseCreators.withSuccess(json, MediaType.APPLICATION_JSON));
        assertThat(sipService.getPlatforms().getSipTwPlatforms())
                .isEqualTo(mockExternalServicePlatformsResponseList());
    }

    private String fromFile(String path) throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(getClass().getResource(path).toURI())));
    }

    private List<SipTwDeparture> mockExternalServiceDeparturesResponseList() {
        SipTwDeparture departure = new SipTwDeparture("ANNOPOL", "1", "0");
        SipTwDeparture departure2 = new SipTwDeparture("METRO WILANOWSKA", "14", "1");
        SipTwDeparture departure3 = new SipTwDeparture("ANNOPOL", "25", "5");
        return Arrays.asList(departure, departure2, departure3);
    }

    private List<SipTwPlatform> mockExternalServicePlatformsResponseList() {
        SipTwPlatform platform = new SipTwPlatform(100103, "KIJOWSKA [03]", "KINO FEMINA");
        SipTwPlatform platform2 = new SipTwPlatform(100104, "KIJOWSKA [04]", "ZĄBKOWSKA");
        SipTwPlatform platform3 = new SipTwPlatform(100303, "DWORZEC WILEŃSKI [03]", "PARK PRASKI");
        return Arrays.asList(platform, platform2, platform3);
    }
}