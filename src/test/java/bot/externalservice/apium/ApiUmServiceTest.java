package bot.externalservice.apium;

import bot.externalservice.apium.configuration.ApiUmConfiguration;
import bot.externalservice.apium.impl.ApiUmServiceImpl;
import bot.externalservice.apium.response.ApiUmDeparture;
import bot.externalservice.apium.response.ApiUmResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestTemplate.class, ApiUmServiceImpl.class, ApiUmConfiguration.class})
public class ApiUmServiceTest {

    @Autowired
    private ApiUmService apiUmService;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private ApiUmConfiguration apiUmConfiguration;

    private MockRestServiceServer mockServer;

    @Before
    public void init() {
        Mockito.when(apiUmConfiguration.getKey()).thenReturn("fakeKey");
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testApiUmServiceResponse_whenExternalServiceRespondsWithData() throws IOException, URISyntaxException {
        String json = fromFile("/apium/fabrykaPomp.json");
        mockServer.expect(MockRestRequestMatchers
                .requestTo("https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238&busstopId=1092&busstopNr=01&line=N64&apikey=fakeKey"))
                .andRespond(MockRestResponseCreators.withSuccess(json, MediaType.APPLICATION_JSON));
        assertThat(apiUmService.getDepartureDetails("1092", "01", "N64").getDepartures()).
                isEqualTo(mockExternalServiceDeparturesResponseList());
    }

    @Test
    public void testApiUmServiceResponse_whenExternalServiceRespondsWithEmptyArray() throws IOException, URISyntaxException {
        String json = fromFile("/apium/emptyResponse.json");
        mockServer.expect(MockRestRequestMatchers
                .requestTo("https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238&busstopId=1092&busstopNr=01&line=N64&apikey=fakeKey"))
                .andRespond(MockRestResponseCreators.withSuccess(json, MediaType.APPLICATION_JSON));
        assertThat(apiUmService.getDepartureDetails("1092", "01", "N64").getDepartures()).
                isEqualTo(Collections.emptyList());
    }

    @Test
    public void testApiUmServiceResponse_whenInvalidApiKeyIsSent() throws IOException, URISyntaxException {
        String json = fromFile("/apium/wrongApiKey.json");
        mockServer.expect(MockRestRequestMatchers
                .requestTo("https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238&busstopId=1092&busstopNr=01&line=N64&apikey=fakeKey"))
                .andRespond(MockRestResponseCreators.withSuccess(json, MediaType.APPLICATION_JSON));
        ApiUmResponse apiUmServiceResponse = apiUmService.getDepartureDetails("1092", "01", "N64");
        assertThat(apiUmServiceResponse.isSuccess()).isEqualTo(false);
        assertThat(apiUmServiceResponse.getDepartures()).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testApiUmServiceResponse_whenInvalidRequestIsSent() throws IOException, URISyntaxException {
        String json = fromFile("/apium/missingParameter.json");
        mockServer.expect(MockRestRequestMatchers
                .requestTo("https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238&busstopId=1092&busstopNr=01&line=N64&apikey=fakeKey"))
                .andRespond(MockRestResponseCreators.withSuccess(json, MediaType.APPLICATION_JSON));
        ApiUmResponse apiUmServiceResponse = apiUmService.getDepartureDetails("1092", "01", "N64");
        assertThat(apiUmServiceResponse.isSuccess()).isEqualTo(false);
        assertThat(apiUmServiceResponse.getDepartures()).isEqualTo(Collections.emptyList());
    }

    private List<ApiUmDeparture> mockExternalServiceDeparturesResponseList() {
        ApiUmDeparture apiUmDeparture = new ApiUmDeparture("N64", "Dw.Centralny", "23:23:00");
        ApiUmDeparture apiUmDeparture2 = new ApiUmDeparture("N64", "Dw.Centralny", "23:57:00");
        return Arrays.asList(apiUmDeparture, apiUmDeparture2);
    }

    private String fromFile(String path) throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(getClass().getResource(path).toURI())));
    }
}
