package bot.externalservice.apium.impl;

import bot.externalservice.apium.ApiUmService;
import bot.externalservice.apium.configuration.ApiUmConfiguration;
import bot.externalservice.apium.dto.GetTimetableResponseWrapper;
import bot.externalservice.apium.response.ApiUmResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class ApiUmServiceImpl implements ApiUmService {

    private static final String SERVICE_URL = "https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238";

    private final RestTemplate restTemplate;

    private ApiUmConfiguration apiUmConfiguration;

    @Autowired
    public ApiUmServiceImpl(ApiUmConfiguration apiUmConfiguration, RestTemplate restTemplate) {
        this.apiUmConfiguration = apiUmConfiguration;
        this.restTemplate = restTemplate;
    }

    @Override
    public ApiUmResponse getDepartureDetails(String stationId, String platformNumber, String line) {
        String uri = UriComponentsBuilder.fromHttpUrl(SERVICE_URL)
                .queryParam("busstopId", stationId)
                .queryParam("busstopNr", platformNumber)
                .queryParam("line", line)
                .queryParam("apikey", apiUmConfiguration.getKey())
                .toUriString();

        log.info("Calling API UM at " + uri + " for departure details");
        GetTimetableResponseWrapper getTimetableResponseWrapper = restTemplate.getForObject(uri, GetTimetableResponseWrapper.class);

        ApiUmResponse response = ApiUmResponse.failed();
        if (getTimetableResponseWrapper != null) {
            if (getTimetableResponseWrapper.isSuccess()) {
                response = new ApiUmResponse(getTimetableResponseWrapper, line);
            } else {
                log.error("API UM responded with error. Reason: " + getTimetableResponseWrapper.getMessage());
            }
        }
        return response;
    }
}
