package bot.processor;

import bot.schema.Response;
import bot.utils.StringHelper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Data
@Service
public class QueryProcessor {

    private TimetableProcessor timetableProcessor;

    @Autowired
    public QueryProcessor(TimetableProcessor timetableProcessor) {
        this.timetableProcessor = timetableProcessor;
    }

    public Response getFullResponse(String messageText) {
        String body = StringHelper.sanitizeInput(messageText);
        List<String> bodyExploded = Arrays.asList(body.split(" "));

        String lastEl = bodyExploded.get(bodyExploded.size()-1);

        int lastNumber = Integer.MAX_VALUE;
        if(StringHelper.isNumeric(lastEl)) {
            lastNumber = Integer.valueOf(lastEl);
        }
        return timetableProcessor.processQuery(bodyExploded, lastNumber);
    }
}
