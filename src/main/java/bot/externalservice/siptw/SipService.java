package bot.externalservice.siptw;

import bot.externalservice.siptw.data.Departure;
import bot.externalservice.siptw.data.Platform;
import bot.externalservice.siptw.data.PlatformRaw;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.net.URISyntaxException;

@Service
@Slf4j
@EnableRetry
public class SipService {

    private static final String SIP_SERVICE_URL = "https://tw.waw.pl/wp-admin/admin-ajax.php";

    private RestTemplate restTemplate;

    @Autowired
    public SipService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    @Retryable
    public SipServiceResponse getTimetableForPlatform(int platformID) throws URISyntaxException {

        HttpEntity<MultiValueMap<String, String>> request = configureRequest(platformID);
        log.info("Calling SIP at " + SIP_SERVICE_URL + " for details about platform with ID: " + platformID);

        Departure[] departures = restTemplate.postForObject(SIP_SERVICE_URL, request, Departure[].class);
        return new SipServiceResponse(departures);
    }

    @Retryable
    public SipServiceResponse getPlatforms() throws  URISyntaxException {
        HttpEntity<MultiValueMap<String, String>> request = configureRequest();
        log.info("Calling SIP at " + SIP_SERVICE_URL + " for details about platforms");
        PlatformRaw[] platforms = restTemplate.postForObject(SIP_SERVICE_URL,request,PlatformRaw[].class);
        return new SipServiceResponse(platforms);
    }

    private HttpEntity<MultiValueMap<String, String>> configureRequest(int platformID) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("action", "sip_get_llegadas_parada");
        map.add("id", String.valueOf(platformID));

        return new HttpEntity<>(map, headers);
    }

    private HttpEntity<MultiValueMap<String,String>> configureRequest(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("action","sip_get_paradas");
        map.add("idlinea","");

        return new HttpEntity<>(map,headers);
    }
}