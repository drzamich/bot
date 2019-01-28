package bot;

import bot.processor.DataManager;
import bot.processor.QueryProcessor;
import bot.processor.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
//@EnableAutoConfiguration
//@EnableJpaRepositories
public class Controller {

    @Autowired
    private QueryProcessor queryProcessor;

    @Autowired
    private DataManager dataManager;

    @RequestMapping(value= "msg/{msg}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String giveInfo(@PathVariable("msg") String msg){
        this.queryProcessor.processQuery(msg);
        System.out.println(queryProcessor.getResponse().getConsoleInfo());
        return queryProcessor.getResponse().getConsoleInfo();
    }

    @RequestMapping(value= "lists", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void giveInfo(){
        dataManager.prepareData();
    }

    @PostMapping("/getJSON")
    public String getJSON(@RequestBody String query){
        query = Utilities.parsePostRequest(query);
        this.queryProcessor.processQuery(query);
        return queryProcessor.getResponse().getResponseJSONString();
    }

}
