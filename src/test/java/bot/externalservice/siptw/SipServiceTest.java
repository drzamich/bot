package bot.externalservice.siptw;

import bot.externalservice.siptw.response.SipServiceDepartureResponse;
import bot.externalservice.siptw.schema.DepartureSipTw;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SipServiceTest {

    private static final String SIP_SERVICE_URL = "https://tw.waw.pl/wp-admin/admin-ajax.php";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private SipService sipService;

    @Before
    public void init() {
        Mockito.when(restTemplateBuilder.build()).thenReturn(restTemplate);
        sipService = new SipService(restTemplateBuilder);
    }

    @Test
    public void testSipServiceDepartureResponse() {
        DepartureSipTw[] externalServiceResponse = mockExternalServiceResponse();

        Mockito.when(restTemplate.postForObject(ArgumentMatchers.matches(SIP_SERVICE_URL),
                ArgumentMatchers.any(), ArgumentMatchers.eq(DepartureSipTw[].class))).thenReturn(externalServiceResponse);
        SipServiceDepartureResponse expected = new SipServiceDepartureResponse(externalServiceResponse);
        assertThat(sipService.getTimetableForPlatform(323904)).isEqualTo(expected);
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject(ArgumentMatchers.matches(SIP_SERVICE_URL),
                ArgumentMatchers.any(), ArgumentMatchers.eq(DepartureSipTw[].class));
    }

    private DepartureSipTw[] mockExternalServiceResponse() {
        DepartureSipTw departureSipTw = new DepartureSipTw();
        departureSipTw.setDestination("TARCHOMIN KOŚCIELNY");
        departureSipTw.setLine("17");
        departureSipTw.setTimeMinutes("3");
        DepartureSipTw departureSipTw2 = new DepartureSipTw();
        departureSipTw2.setDestination("TARCHOMIN KOŚCIELNY");
        departureSipTw2.setLine("17");
        departureSipTw2.setTimeMinutes("24");
        return new DepartureSipTw[]{departureSipTw, departureSipTw2};
    }
}