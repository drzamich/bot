package bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
public class StationService {

    @Autowired
    AcceptedNameRepository acceptedNameRepository;

    public String getInfo(String name, String dir) {
        try {
            name = Utilities.parseInput(name);
            dir = Utilities.parseInput(dir);
            Station station = acceptedNameRepository.findByNameAccepted(name).getStation();
            Platform searchedPlatform = Utilities.getPlatformByDirection(station.getPlatforms(), dir);

            InformationSchema schema = new InformationSchema(station, searchedPlatform);
            return schema.toString();
        } catch (NullPointerException e) {
            return "Wrong station name or direction";
        }
    }
}
