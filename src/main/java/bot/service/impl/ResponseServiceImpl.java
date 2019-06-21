package bot.service.impl;

import bot.schema.StationRequest;
import bot.schema.dto.DepartureDto;
import bot.schema.dto.StationDto;
import bot.schema.response.*;
import bot.service.DepartureService;
import bot.service.ResponseService;
import bot.service.StationMatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService {

    private static final int MULTIPLE_STATIONS_THRESHOLD = 12;

    private final StationMatcherService stationMatcherService;
    private final DepartureService departureService;

    @Autowired
    public ResponseServiceImpl(StationMatcherService stationMatcherService, DepartureService departureService) {
        this.stationMatcherService = stationMatcherService;
        this.departureService = departureService;
    }

    @Override
    public Response getResponse(String requestString) {
        Response response;
        StationRequest stationRequest = new StationRequest(requestString);
        if (stationRequest.isValid(requestString)) {
            List<StationDto> matchingStations = stationMatcherService.getMatchingStations(stationRequest.getStationName());
            if(matchingStations.size() == 1) {
                response = handleSingleStationFound(matchingStations.get(0), stationRequest.containsPlatformNumber());
            } else {
                response = handleAmbiguousRequest(matchingStations);
            }
        } else {
            response = new SimpleErrorResponse("No stations found, please refine your query");
        }
        return response;
    }

    private Response handleSingleStationFound(StationDto matchingStation, boolean platformNumberProvided) {
        Response response;
        if (platformNumberProvided) {
            List<DepartureDto> departureDtos = departureService.getDepartures(matchingStation);
            response = new DeparturesResponse(departureDtos);
        } else {
            response = new ChoosePlatformResponse(matchingStation);
        }
        return response;
    }

    private Response handleAmbiguousRequest(List<StationDto> matchingStations) {
        Response response;
        if (matchingStations.isEmpty()) {
            response = new SimpleErrorResponse("No stations found, please refine your query");
        } else if (matchingStations.size() < MULTIPLE_STATIONS_THRESHOLD) {
            response = new MultipleStationsFoundResponse(matchingStations);
        } else {
            response = new SimpleErrorResponse("Too many stations found, please refine your query.");
        }
        return response;
    }

}
