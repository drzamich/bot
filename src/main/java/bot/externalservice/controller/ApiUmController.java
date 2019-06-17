package bot.externalservice.controller;

import bot.externalservice.apium.ApiUmService;
import bot.externalservice.apium.response.ApiUmResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiUmController {

    private ApiUmService apiUmService;

    @Autowired
    public ApiUmController(ApiUmService apiUmService) {
        this.apiUmService = apiUmService;
    }

    @GetMapping("apium/departures")
    public ApiUmResponse getApiUmDeparturesShortcut(@RequestParam String stationId, @RequestParam String platformNumber, @RequestParam String line) {
        return apiUmService.getDepartureDetails(stationId, platformNumber, line);
    }
}
