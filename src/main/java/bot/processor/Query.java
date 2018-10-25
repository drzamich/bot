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

    public Query(String body){
        this.body = body;
        this.bodyExploded = Arrays.asList(body.split(" "));
    }
}
