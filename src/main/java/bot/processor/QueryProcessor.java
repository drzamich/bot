package bot.processor;

import bot.schema.Response;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Data
@Service
public class QueryProcessor {
    private String body;
    private List<String> bodyExploded;
    private String[] properties;
    private boolean settingsQuery = false;
    private boolean toSipTw = true;
    private boolean toApiUm = false;
    private int lastNumber;
    private Response response;

    @Autowired
    TimetableProcessor timetableProcessor;

    public QueryProcessor() {

    }

    public void parseQuery(String messageText) {
        this.body = Utilities.parseInput(messageText);
        this.bodyExploded = Arrays.asList(body.split(" "));

        String lastEl = this.bodyExploded.get(this.bodyExploded.size()-1);

        if(Utilities.isNumeric(lastEl)) {
            this.lastNumber = Integer.valueOf(lastEl);
        }
        else {
            this.lastNumber = 999;
        }
    }

    public Response getFullResponse() {
        this.response = timetableProcessor.processQuery(this.bodyExploded,this.lastNumber);
        return this.response;
    }
}
