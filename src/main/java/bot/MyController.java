package bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MyController {


   // @Autowired
    StationService stationService;

    @RequestMapping(value = "/find/{name}/{dir}", method = RequestMethod.GET)
    public String find(@PathVariable("name") String name, @PathVariable("dir") String dir) {
        return stationService.getInfo(name,dir);
    }

}
