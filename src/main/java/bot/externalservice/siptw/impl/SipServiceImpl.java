package bot.externalservice.siptw.impl;

import bot.externalservice.siptw.SipService;
import bot.externalservice.siptw.configuration.SipTwConfiguration;
import bot.externalservice.siptw.dto.SipTwDepartureDto;
import bot.externalservice.siptw.dto.SipTwPlatformDto;
import bot.externalservice.siptw.response.SipTwDeparturesResponse;
import bot.externalservice.siptw.response.SipTwPlatformsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@EnableRetry
@Service
public class SipServiceImpl implements SipService {

    private static final String SIP_SERVICE_URL = "https://public-sip-api.tw.waw.pl/api/";

    private final RestTemplate restTemplate;
    private final SipTwConfiguration sipTwConfiguration;

    @Autowired
    public SipServiceImpl(RestTemplate restTemplate, SipTwConfiguration sipTwConfiguration) {
        this.restTemplate = restTemplate;
        this.sipTwConfiguration = sipTwConfiguration;
    }

    @Retryable
    @Override
    public SipTwDeparturesResponse getTimetableForPlatform(int platformID) {
        String endpoint = "GetLatestPanelPredictions";
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("userApiKey", sipTwConfiguration.getKey());
        parameters.add("stopId", String.valueOf(platformID));
        URI uri = configureRequestUrl(endpoint, parameters);
        log.info("Calling SIP at " + uri.toString() + " for details about platform with ID: " + platformID);
        SipTwDepartureDto[] departures = restTemplate.getForObject(uri, SipTwDepartureDto[].class);
        return new SipTwDeparturesResponse(departures);
    }

    @Retryable
    @Override
    public SipTwPlatformsResponse getPlatforms() {
        String endpoint = "GetStops";
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("userApiKey", sipTwConfiguration.getKey());
        URI uri = configureRequestUrl(endpoint, parameters);

        log.info("Calling SIP at " + uri.toString() + " for details about platforms");

        SipTwPlatformDto[] sipTwPlatformDtos = restTemplate.getForObject(uri, SipTwPlatformDto[].class);
        return new SipTwPlatformsResponse(sipTwPlatformDtos);
    }

    private URI configureRequestUrl(String endpointPath, MultiValueMap<String, String> map) {
        map.add("userCode", "WWW");
        return UriComponentsBuilder.fromHttpUrl(SIP_SERVICE_URL).path(endpointPath).queryParams(map).build(true).toUri();
    }
}