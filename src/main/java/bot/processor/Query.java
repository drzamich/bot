package bot.processor;

import lombok.Data;

@Data
public class Query {
    private String body;
    private String[] bodyExploded;
    private String[] properties;
    private boolean toSipTw = true;
    private boolean toApiUm = false;

    public Query(String body){
        this.body = body;
        this.bodyExploded = body.split(" ");
    }
}
