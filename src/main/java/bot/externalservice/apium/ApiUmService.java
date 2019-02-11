package bot.externalservice.apium;

import bot.Settings;
import bot.schema.Departure;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class ApiUmService {

    private static final String SERVICE_URL = "https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238";

    private RestTemplate restTemplate = new RestTemplate();

    protected List<Departure> getDepartureDetails(String stationId, String platformNumber, String line) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(SERVICE_URL)
                .queryParam("busstopId", stationId)
                .queryParam("busstopNr", platformNumber)
                .queryParam("line", line)
                .queryParam("apikey", Settings.APIUM_API_KEY);

        ResponseEntity<ApiUmResponse> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<ApiUmResponse>() {
                });

        ApiUmResponse responseObject = response.getBody();
        return responseObject.parseDepartureDetails(line);
    }
}
