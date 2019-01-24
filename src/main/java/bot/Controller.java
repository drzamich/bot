package bot;

import bot.processor.DataManager;
import bot.processor.QueryProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
//@EnableJpaRepositories
public class Controller {


    @Autowired
    QueryProcessor queryProcessor;

    @Autowired
    DataManager dataManager;

    @RequestMapping(value= "msg/{msg}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String giveInfo(@PathVariable("msg") String msg){
        queryProcessor.processQuery(msg);
        System.out.println(queryProcessor.getResponse().getInfo());
        return queryProcessor.getResponse().getInfo();
    }

    @RequestMapping(value= "lists", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void giveInfo(){
        dataManager.prepareData();
    }

}
