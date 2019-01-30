package bot.processor;

import bot.schema.Response;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
@Data
public class QueryProcessor {

    private Query query;
    private Response response;

    @Autowired
    TimetableProcessor timetableProcessor;

    public QueryProcessor() {
    }

    public void processQuery(String msg){
        msg = Utilities.parseInput(msg);
        this.query = new Query(msg);

        if(this.query.isSettingsQuery()){

        }
        else{
            this.response = timetableProcessor.processQuery(this.query);
        }
    }

    public void processPostQuery(String msg){
        String userId = msg.substring(msg.lastIndexOf(" ")+1);
        msg = msg.substring(0, msg.lastIndexOf(" "));
        msg = Utilities.parseInput(msg);
        this.query = new Query(msg, userId);

        if(this.query.isSettingsQuery()){

        }
        else{
            this.response = timetableProcessor.processQuery(this.query);
        }
    }
}
