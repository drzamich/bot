package bot.externalservice.apium;

import bot.configuration.ApiUmConfiguration;
import bot.externalservice.apium.dto.ApiUmResponseDtoWrapper;
import bot.externalservice.apium.response.ApiUmResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class ApiUmService {

    private static final String SERVICE_URL = "https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238";

    private final RestTemplate restTemplate;

    private ApiUmConfiguration apiUmConfiguration;

    @Autowired
    public ApiUmService(ApiUmConfiguration apiUmConfiguration, RestTemplate restTemplate) {
        this.apiUmConfiguration = apiUmConfiguration;
        this.restTemplate = restTemplate;
    }

    public ApiUmResponse getDepartureDetails(String stationId, String platformNumber, String line) {
        String uri = UriComponentsBuilder.fromHttpUrl(SERVICE_URL)
                .queryParam("busstopId", stationId)
                .queryParam("busstopNr", platformNumber)
                .queryParam("line", line)
                .queryParam("apikey", apiUmConfiguration.getKey())
                .toUriString();
        ApiUmResponseDtoWrapper apiUmResponseDtoWrapper = restTemplate.getForObject(uri, ApiUmResponseDtoWrapper.class);

        ApiUmResponse response = ApiUmResponse.failed();
        if (apiUmResponseDtoWrapper != null) {
            if (apiUmResponseDtoWrapper.isSuccess()) {
                response = new ApiUmResponse(apiUmResponseDtoWrapper, line);
            } else {
                log.error("API UM responded with error. Reason: " + apiUmResponseDtoWrapper.getMessage());
            }
        }
        return response;
    }
}
