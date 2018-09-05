package bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MyController {

    @Autowired
    AcceptedNameRepository acceptedNameRepository;

    @RequestMapping(value = "/find/{name}/{dir}", method = RequestMethod.GET)
    public String find(@PathVariable("name") String name, @PathVariable("dir") String dir) {
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
