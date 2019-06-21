package bot.service;

import bot.schema.dto.StationDto;

import java.util.List;

public interface StationMatcherService {
    List<StationDto> getMatchingStations(String stationName);
}
