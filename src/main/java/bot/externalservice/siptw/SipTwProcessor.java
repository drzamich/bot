package bot.externalservice.siptw;

import bot.externalservice.siptw.data.*;
import bot.processor.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SipTwProcessor {

    @Autowired
    AcceptedNameRepository acceptedNameRepository;

    @Autowired
    SipService sipService;

    private String[] parts;
    Optional<Station> stationOptional;
    SipServiceResponse sipServiceResponse;

    public void processQuery(Query query){
        parts = query.getBodyExploded();
        stationOptional = matchStation(parts);
        if (!stationOptional.isPresent()) {
            //station not given
            System.out.println("Wrong station name");
        }
        Station station = stationOptional.get();
        List<Platform> platforms = station.getPlatforms();
        Optional<Platform> platformOptional = matchPlatform(parts, platforms);

        Platform platform;
        if (!platformOptional.isPresent()) {
            // platform not given
            // ask user for platform directions
            platform = platforms.get(0); //temporary solution for checking station names
        }
        else {
            platform = platformOptional.get();
        }
        int platformId = platform.getPlatformId();

        try {
            sipServiceResponse = sipService.getTimetableForPlatform(platformId);
            System.out.println(sipServiceResponse);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    private Optional<Station> matchStation(String[] parts) {
        int elements = parts.length;

        String testedName = parts[0];
        Optional<AcceptedName> acceptedName;
        for (int i = 0; i < elements; i++) {
            if (i != 0) {
                testedName = testedName + " " + parts[i];
            }
            acceptedName = acceptedNameRepository.findByNameAccepted(testedName);
            if (acceptedName.isPresent()) {
                return Optional.of(acceptedName.get().getStation());
            }
        }
        return Optional.empty();
    }

    private Optional<Platform> matchPlatform(String[] parts, List<Platform> platforms) {
        for (Platform platform : platforms) {
            String platformNumber = String.valueOf(platform.getPlatformNumber());
            List<Direction> directions = platform.getDirections();
            for (String part : parts) {
                for (Direction direction : directions) {
                    if (platformNumber.equals(part) || direction.getDirNameAccepted().equals(part)) {
                        return Optional.of(platform);
                    }
                }
            }
        }
        return Optional.empty();
    }
}
