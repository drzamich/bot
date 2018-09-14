package bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyController {


   @Autowired
    StationService stationService;

    @RequestMapping(value= "msg/{msg}", method = RequestMethod.GET)
    public String giveInfo(@PathVariable("msg") String msg){
        return stationService.getInformation(msg);
    }

}
