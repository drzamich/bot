package bot.schema;

import java.util.List;

public class JSONResponse {
    private List<Button> buttonList;
    private List<String> messages;

    public JSONResponse(List<Button> buttonList, List<String> messages) {
        this.buttonList = buttonList;
        this.messages = messages;
    }

//    @Override
//    public String toString() {
//
//    }
}
