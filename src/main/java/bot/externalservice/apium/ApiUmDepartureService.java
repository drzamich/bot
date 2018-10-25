package bot.externalservice.apium;

import bot.data.Departure;
import bot.externalservice.apium.data.DeparturesListWithTimes;
import bot.externalservice.apium.data.Platform;
import bot.externalservice.apium.data.Station;
import bot.processor.Utilities;
import lombok.Data;
import org.apache.commons.collections4.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class ApiUmDepartureService extends DataManager {
    private Station station;


    @Autowired
    ApiUmService apiUmService = new ApiUmService(new RestTemplateBuilder());

    public ApiUmDepartureService(Station station) {
        this.station = station;
    }

    public List<Departure> getDeparturesForPlatform(Platform platform) {
        String platformNumber = platform.getNumber();
        String identifier = this.date + "_" + this.station.getId() + "_" + platformNumber;
        String path = PATH_TO_OBJECTS + identifier;

        if (Utilities.objectExists(path)) {
            return calculateDeparturesList(Utilities.deserializeObject(path));
        } else {
            return getDeparturesLists(platform, path);
        }
    }

    private List<Departure> calculateDeparturesList(DeparturesListWithTimes departuresListWithTimes) {
        List<Departure> departuresListCalculated = new ArrayList<>();
        List<String> times = departuresListWithTimes.getTimes();
        MultiValueMap<String, Departure> mappedDepartures = departuresListWithTimes.getMappedDepartures();

        String currTime = Utilities.getTime(TIME_PATTERN);
        for (String time : times) {
            int timeDiff = Math.round(Utilities.compareTimes(currTime, time, TIME_PATTERN) / 60000);
            if (timeDiff >= 0) {
                List<Departure> deps = (List<Departure>) mappedDepartures.get(time);
                for (Departure dep : deps) {
                    Departure departure = new Departure(dep.getLine(), dep.getDirection(), Integer.toString(timeDiff));
                    departuresListCalculated.add(departure);
                }
            }
        }
        return departuresListCalculated;
    }

    private List<Departure> getDeparturesLists(Platform platform, String path) {
        String platformNumber = platform.getNumber();
        List<String> lines = platform.getLines();
        MultiValueMap<String, Departure> mappedDepartures = new MultiValueMap<>();
        List<String> times = new ArrayList<>();

        List<List<Departure>> departures =
                lines.parallelStream()
                        .map(l -> apiUmService.getDepartureDetails(this.station.getId(), platformNumber, l))
                        .collect(toList());


        for (List<Departure> list : departures) {
            for (Departure dep : list) {
                String time = dep.getTime();
                mappedDepartures.put(time, dep);
                if (!times.contains(time)) {
                    times.add(time);
                }
            }
        }

        Collections.sort(times);
        DeparturesListWithTimes departuresListWithTimes = new DeparturesListWithTimes(times, mappedDepartures);
        Utilities.serializeObject(departuresListWithTimes, path);
        return calculateDeparturesList(departuresListWithTimes);

    }

}

