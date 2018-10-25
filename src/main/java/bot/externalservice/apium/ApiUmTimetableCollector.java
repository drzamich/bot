package bot.externalservice.apium;

import bot.schema.Platform;
import bot.schema.Station;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Data
public class ApiUmTimetableCollector extends Properties {
    private Station station;
    private List<Platform> platforms;
    private ApiUmTimetableGenerator departureService;

    public ApiUmTimetableCollector(){

    }

    public ApiUmTimetableCollector(Station station) {
        super();
        this.station = station;
        this.departureService = new ApiUmTimetableGenerator(this.station);
        this.platforms = station.getPlatforms();
        this.processPlatforms();
    }

    private void processPlatforms() {
        this.platforms.parallelStream()
                .forEach(s -> this.departureService.getDeparturesForPlatform(s));
    }

}

