package bot.processor;

import bot.externalservice.siptw.SipTwProcessor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Data
public class QueryProcessor {

    @Autowired
    SipTwProcessor sipTwProcessor;

    private Query query;

    public void processQuery(String msg){
        this.query = new Query(msg);

        if(query.isToSipTw()){
            sipTwProcessor.processQuery(this.query);
        }

    }
}
