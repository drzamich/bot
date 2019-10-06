package bot;

import bot.processor.DataManager;
import bot.processor.QueryProcessor;
import bot.schema.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class Controller {

    @Autowired
    QueryProcessor queryProcessor;

    @RequestMapping(value= "msg/{msg}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String giveInfo(@PathVariable("msg") String msg){
        queryProcessor.parseQuery(msg);
        Response response = queryProcessor.getFullResponse();
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
