package bot.externalservice.apium;

import bot.schema.Departure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class ApiUmService extends Properties {
    private RestTemplate restTemplate;

    @Autowired
    public ApiUmService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    protected List<Departure> getDepartureDetails(String stationId, String platformNumber, String line) {
        List<Departure> res = new ArrayList<>();
        try {
            String urlToApi = "https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238" +
                    "&busstopId=" + stationId + "&busstopNr=" + platformNumber + "&line=" + line + "&apikey=" + API_KEY;

            RestTemplate restTemplate2 = new RestTemplate();
            ResponseEntity<ApiUmResponse> response = restTemplate2.exchange(urlToApi, HttpMethod.GET, null,
                    new ParameterizedTypeReference<ApiUmResponse>() {
                    });
            ApiUmResponse responseObject = response.getBody();
            res = responseObject.parseDepartureDetails(line);
        }
        catch (Exception e){
        }
        return res;
    }
}
