package bot.externalservice.apium;

import bot.Settings;
import bot.schema.Departure;
import bot.externalservice.apium.schema.DeparturesListWithTimes;
import bot.schema.Platform;
import bot.schema.Station;
import bot.utils.FileHelper;
import bot.utils.StringHelper;
import lombok.Data;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@Service
public class ApiUmTimetableGenerator {

    private ApiUmService apiUmService;

    @Autowired
    public ApiUmTimetableGenerator(ApiUmService apiUmService) {
        this.apiUmService = apiUmService;
    }

    void generateTimetablesForStation(Station station){
        station.getPlatforms().parallelStream()
                .forEach(p -> getDeparturesForPlatform(p, station));
    }

    public List<Departure> getDeparturesForPlatform(Platform platform, Station station) {
        String platformNumber = platform.getNumber();
        String identifier = StringHelper.getTime(Settings.DATE_PATTERN) + "_" + station.getId() + "_" + platformNumber;
        String path = Settings.PATH_SAVED_TIMETABLES + identifier;
        if (FileHelper.fileExists(path)) {
            return calculateDeparturesList(FileHelper.deserializeObject(path));
        } else {
            return getDeparturesLists(platform, station, path);
        }
    }

    private List<Departure> calculateDeparturesList(DeparturesListWithTimes departuresListWithTimes) {
        List<Departure> departuresListCalculated = new ArrayList<>();
        List<String> times = departuresListWithTimes.getTimes();
        MultiValuedMap<String, Departure> mappedDepartures = departuresListWithTimes.getMappedDepartures();
        String currTime = StringHelper.getTime(Settings.TIME_PATTERN);
        for (String time : times) {
            int timeDiff = Math.round(StringHelper.getTimeDifferenceInMilliseconds(currTime, time, Settings.TIME_PATTERN) / 60000);
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

    private List<Departure> getDeparturesLists(Platform platform, Station station, String path) {
        String platformNumber = platform.getNumber();
        List<String> lines = platform.getLines();
        MultiValuedMap<String, Departure> mappedDepartures = new ArrayListValuedHashMap<>();
        List<String> times = new ArrayList<>();

        List<List<Departure>> departures =
                lines.parallelStream()
                        .map(l -> apiUmService.getDepartureDetails(station.getId(), platformNumber, l))
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
        FileHelper.serializeObject(departuresListWithTimes, path);
        return calculateDeparturesList(departuresListWithTimes);
    }
}

