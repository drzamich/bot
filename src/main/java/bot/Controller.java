package bot;

import bot.processor.DataManager;
//import bot.processor.QueryProcessor;
//import bot.processor.Query;
import bot.processor.QueryProcessor;
import bot.schema.Response;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@EnableAutoConfiguration
//@EnableJpaRepositories
public class Controller {

//    @Autowired
//    private QueryProcessor queryProcessor;

    @Autowired
    private DataManager dataManager;

    @Autowired
    QueryProcessor queryProcessor;

    @RequestMapping(value= "msg/{msg}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String giveInfo(@PathVariable("msg") String msg){
//        queryProcessor.parseQuery(msg);
//        Response response = queryProcessor.getFullResponse();
//        List<String> messages = response.getMessages();
//        String quickRepliesHint = response.getQuickRepliesObject().getHintMessage();
//        List<QuickReply> quickReplies = response.getQuickRepliesObject().getQuickReplyList();
//        System.out.println(messages);
//        System.out.println(quickReplies);
//        return "YES";

//        queryProcessor.parseQuery(msg);
//        System.out.println(queryProcessor.getFullResponse().getConsoleInfo());
//        return queryProcessor.getFullResponse().getConsoleInfo();
        return "a";
    }

    @RequestMapping(value= "lists", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void giveInfo(){
        dataManager.prepareData();
    }

//    @PostMapping("/getJSON")
//    public String getJSON(@RequestBody String query){
//        query = Utilities.parsePostRequest(query);
//        this.queryProcessor.processPostQuery(query);
////        return queryProcessor.getFullResponse().getResponseJSONString();
//    }

    @PostMapping(value = "/test", produces = "text/plain")
    @ResponseBody
    public String getJSON(){
        return "No quotes please";
    }

}
