package bot.service.impl;

import bot.externalservice.siptw.SipService;
import bot.externalservice.siptw.response.SipTwDeparturesResponse;
import bot.schema.TimetableResponse;
import bot.schema.dto.DepartureDto;
import bot.schema.dto.StationDto;
import bot.service.DepartureService;
import bot.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DepartureServiceImpl implements DepartureService {

    private final SipService sipService;

    private final TimetableService timetableService;

    @Autowired
    public DepartureServiceImpl(SipService sipService, TimetableService timetableService) {
        this.sipService = sipService;
        this.timetableService = timetableService;
    }

    @Override
    public List<DepartureDto> getDepartures(StationDto station) {
        return Optional.of(station)
                .filter(StationDto::isAtSipTw)
                .map(StationDto::getId)
                .map(sipService::getTimetableForPlatform)
                .map(this::toDepartures)
                .orElseGet(() -> fallbackToTimetable(station));
    }

    private List<DepartureDto> toDepartures(SipTwDeparturesResponse sipTwDeparturesResponse) {
        return Optional.of(sipTwDeparturesResponse)
                .filter(SipTwDeparturesResponse::isValid)
                .map(SipTwDeparturesResponse::getDepartures)
                .map(sipTwDepartures -> sipTwDepartures.stream()
                        .map(DepartureDto::fromSipTwDeparture)
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    private List<DepartureDto> fallbackToTimetable(StationDto station) {
        return Optional.of(timetableService.getTimetableForStation(station))
                .map(this::toDepartures)
                .orElse(Collections.emptyList()); //TODO słabo...
    }

    private List<DepartureDto> toDepartures(TimetableResponse timetableResponse) {
        return null;
    }
}
