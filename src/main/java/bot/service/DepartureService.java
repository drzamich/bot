package bot.service;

import bot.schema.dto.DepartureDto;
import bot.schema.dto.StationDto;

import java.util.List;

public interface DepartureService {
    List<DepartureDto> getDepartures(StationDto stationDto);
}
