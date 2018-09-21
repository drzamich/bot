package bot.processor;

import lombok.Data;

@Data
public class Query {
    private String body;
    private String[] bodyExploded;
    private String[] properties;
    private boolean toSipTw = false;
    private boolean toApiUm = true;

    public Query(String body){
        this.body = body;
        this.bodyExploded = body.split(" ");
    }
}
