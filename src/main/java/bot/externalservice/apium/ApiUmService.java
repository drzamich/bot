package bot.externalservice.apium;

import bot.externalservice.apium.data.ApiUmResponse;
import bot.externalservice.apium.data.DepartureDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Service
public class ApiUmService {
    private RestTemplate restTemplate;

    @Autowired
    public ApiUmService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    protected Optional<List<DepartureDetail>> getDepartureDetails(String stationId, String platformNumber, String line) {
        try {
            String urlToApi = "https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238" +
                    "&busstopId=" + stationId + "&busstopNr=" + platformNumber + "&line=" + line + "&apikey=" + Properties.API_KEY;

            RestTemplate restTemplate2 = new RestTemplate();
            ResponseEntity<ApiUmResponse> response = restTemplate2.exchange(urlToApi, HttpMethod.GET, null,
                    new ParameterizedTypeReference<ApiUmResponse>() {
                    });
            ApiUmResponse responseObject = response.getBody();
            List<DepartureDetail> res = responseObject.parseDepartureDetails(line);
            return Optional.of(res);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
}
