package bot;

import bot.data.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@EnableJpaRepositories
public class Controller {

   @Autowired
   StationService stationService;

    @RequestMapping(value= "msg/{msg}", method = RequestMethod.GET)
    public String giveInfo(@PathVariable("msg") String msg){
        return stationService.getInformation(msg);
    }

}
