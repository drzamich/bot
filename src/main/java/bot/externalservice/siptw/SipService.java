package bot.externalservice.siptw;

import bot.externalservice.siptw.response.SipServiceDepartureResponse;
import bot.externalservice.siptw.response.SipServicePlatformResponse;
import bot.externalservice.siptw.schema.DepartureSipTw;
import bot.externalservice.siptw.schema.PlatformSipTw;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@EnableRetry
@Service
public class SipService {

    private static final String SIP_SERVICE_URL = "https://tw.waw.pl/wp-admin/admin-ajax.php";

    private final RestTemplate restTemplate;

    public SipService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Retryable
    public SipServiceDepartureResponse getTimetableForPlatform(int platformID) {
        HttpEntity<MultiValueMap<String, String>> request = configureRequest("sip_get_llegadas_parada", "id", String.valueOf(platformID));
        log.info("Calling SIP at " + SIP_SERVICE_URL + " for details about platform with ID: " + platformID);
        DepartureSipTw[] departures = restTemplate.postForObject(SIP_SERVICE_URL, request, DepartureSipTw[].class);
        return new SipServiceDepartureResponse(departures);
    }

    @Retryable
    public SipServicePlatformResponse getPlatforms() {
        HttpEntity<MultiValueMap<String, String>> request = configureRequest("sip_get_paradas", "idlinea", "");
        log.info("Calling SIP at " + SIP_SERVICE_URL + " for details about platformSipTws");
        PlatformSipTw[] platformSipTws = restTemplate.postForObject(SIP_SERVICE_URL, request, PlatformSipTw[].class);
        return new SipServicePlatformResponse(platformSipTws);
    }

    private HttpEntity<MultiValueMap<String, String>> configureRequest(String action, String idKey, String idValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("action", action);
        map.add(idKey, idValue);
        return new HttpEntity<>(map, headers);
    }
}