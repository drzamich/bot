package bot.controller;

import bot.processor.DataManager;
import bot.processor.QueryProcessor;
import bot.schema.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class Controller {

    private DataManager dataManager;

    private QueryProcessor queryProcessor;

    @Autowired
    public Controller(DataManager dataManager, QueryProcessor queryProcessor) {
        this.dataManager = dataManager;
        this.queryProcessor = queryProcessor;
    }

    @GetMapping(value= "msg/{msg}")
    @ResponseStatus(value = HttpStatus.OK)
    public String giveInfo(@PathVariable("msg") String msg){
        Response response = queryProcessor.getFullResponse(msg);
        String info = response.consoleInfo();
        System.out.println(info);
        return info;
    }

    @RequestMapping(value= "lists", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void giveInfo(){
        dataManager.prepareData();
    }

}
