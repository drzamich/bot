package bot.processor;

import bot.schema.Response;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Data
public class QueryProcessor {

    private Query query;
    private Response response;

    @Autowired
    TimetableProcessor timetableProcessor = new TimetableProcessor();

    public void processQuery(String msg){
        msg = Utilities.parseInput(msg);
        this.query = new Query(msg);

        if(query.isSettingsQuery()){

        }
        else{
            response = timetableProcessor.processQuery(this.query);
        }

    }
}
