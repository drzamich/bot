package bot.processor;

import lombok.Data;

@Data
public class Query {
    private String body;
    private String[] bodyExploded;
    private String[] properties;
    private boolean toSipTw = false;
    private boolean toApiUm = false;

    public Query(String body){
        this.body = body;
        this.bodyExploded = body.split(" ");
        this.setQueryReceiver();
    }

    private void setQueryReceiver(){
        for(String part : this.bodyExploded){
            if(part.equals("toSipTw")){
                this.toSipTw = true;
            }
            else if(part.equals("toApiUm")){
                this.toApiUm = true;
            }
        }
    }
}
