package bot.processor;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Query {
    private String body;
    private List<String> bodyExploded;
    private String[] properties;
    private boolean settingsQuery = false;
    private boolean toSipTw = true;
    private boolean toApiUm = false;
    private int lastNumber;

    public Query(String body){
        this.body = body;
        this.bodyExploded = Arrays.asList(body.split(" "));

        String lastEl = this.bodyExploded.get(this.bodyExploded.size()-1);

        if(Utilities.isNumeric(lastEl)) {
            this.lastNumber = Integer.valueOf(lastEl);
        }
        else {
            this.lastNumber = 999;
        }
    }
}
