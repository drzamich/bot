package bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StationService {

    @Autowired
    AcceptedNameRepository acceptedNameRepository;
    private boolean platformGiven = false;

//    public String getInfo(String name, String dir) {
//        name = Utilities.parseInput(name);
//        dir = Utilities.parseInput(dir);
//        Optional<AcceptedName> acceptedName = acceptedNameRepository.findByNameAccepted(name);
//
//        if (!acceptedName.isPresent()) {
//            return "Wrong input";
//        }
//
//        Station station = acceptedName.get().getStation();
//        Platform searchedPlatform = Utilities.getPlatformByDirection(station.getPlatforms(), dir);
//        InformationSchema schema = new InformationSchema(station, searchedPlatform);
//        return schema.toString();
//    }

    public String getInformation(String msg) {
        msg = Utilities.parseInput(msg);
        String[] parts = msg.split(" ");
        Optional<Station> stationOptional = matchStation(parts);
        if (!stationOptional.isPresent()) {
            //station not given
            return "Wrong station name";
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
        Curl curl = new Curl();
        Optional<ArrayList<Departure>> departuresListOptional = curl.getDepartureInformation(platform.getPlatformId());

        InformationSchema schema = new InformationSchema(station,platform,departuresListOptional);
        return schema.getInfo();

    }

    private Optional<Station> matchStation(String[] parts) {
        int elements = parts.length;

        String testedName = parts[0];
        for (int i = 0; i < elements; i++) {
            if (i != 0) {
                testedName = testedName + " " + parts[i];
            }
            Optional<AcceptedName> acceptedName = acceptedNameRepository.findByNameAccepted(testedName);
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
