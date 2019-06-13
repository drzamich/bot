package bot.controller.temporary;

import bot.externalservice.ztm.ZtmScraper;
import bot.externalservice.ztm.response.ZtmStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ZtmController {

    private ZtmScraper ztmScraper;

    @Autowired
    public ZtmController(@Qualifier("mockZtm") ZtmScraper scraper) {
        this.ztmScraper = scraper;
    }

    @GetMapping("ztm/stations")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ZtmStation> getStationsShortcut() {
        return ztmScraper.getZtmStationList();
    }
}
