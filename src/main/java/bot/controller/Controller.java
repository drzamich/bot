package bot.controller;

import bot.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class Controller {

    private ResponseService responseService;

    @Autowired
    public Controller(ResponseService responseService) {
        this.responseService = responseService;
    }

    @GetMapping(value= "{station}")
    @ResponseStatus(value = HttpStatus.OK)
    public String getResponse(@PathVariable("station") String stationRequest){
        return responseService.getResponse(stationRequest).getMessage();
    }

}
