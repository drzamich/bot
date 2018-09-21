package bot.processor;

import bot.externalservice.apium.ApiUmProcessor;
import bot.externalservice.siptw.SipTwProcessor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Data
public class QueryProcessor {
    private Response response;

    @Autowired
    SipTwProcessor sipTwProcessor;

    @Autowired
    ApiUmProcessor apiUmProcessor = new ApiUmProcessor();

    private Query query;

    public void processQuery(String msg){
        msg = Utilities.parseInput(msg);
        this.query = new Query(msg);

        if(query.isToSipTw()){
            sipTwProcessor.processQuery(this.query);
        }

        if(query.isToApiUm()){
            response = apiUmProcessor.processQuery(this.query);
        }

        response.prepareMsg();
        for(String s : response.getMessages()){
            System.out.println(s);
        }
    }
}
