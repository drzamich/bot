package bot.externalservice.apium;

import bot.externalservice.apium.response.ApiUmResponse;

public interface ApiUmService {
    ApiUmResponse getDepartureDetails(String stationId, String platformNumber, String line);
}
