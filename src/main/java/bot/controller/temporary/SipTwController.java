package bot.controller.temporary;

import bot.externalservice.siptw.SipService;
import bot.externalservice.siptw.response.SipTwDeparturesResponse;
import bot.externalservice.siptw.response.SipTwPlatformsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SipTwController {

    private SipService sipService;

    @Autowired
    public SipTwController(SipService sipService) {
        this.sipService = sipService;
    }

    @RequestMapping(value= "siptw/platforms", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public SipTwPlatformsResponse getPlatformsShortcut(){
        return sipService.getPlatforms();
    }

    @RequestMapping(value= "siptw/timetable", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public SipTwDeparturesResponse getTimetableForPlatformShortcut(@RequestParam int platformId){
        return sipService.getTimetableForPlatform(platformId);
    }
}