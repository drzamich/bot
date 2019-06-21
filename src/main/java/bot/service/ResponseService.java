package bot.service;

import bot.schema.response.Response;

public interface ResponseService {
    Response getResponse(String stationRequest);
}
