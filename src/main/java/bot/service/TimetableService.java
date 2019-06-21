package bot.service;

import bot.schema.TimetableResponse;
import bot.schema.dto.DepartureDto;
import bot.schema.dto.StationDto;

import java.util.List;

public interface TimetableService {
    TimetableResponse getTimetableForStation(StationDto station);
}
