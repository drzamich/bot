package bot.schema;

import lombok.Data;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class JSONResponse {
    private List<Button> buttonList;
    private ButtonList buttonListObject;
    private List<String> messages;
    private List<String> plainResponses;
    private String userID;
    private String nL = System.getProperty("line.separator");

    public JSONResponse(ButtonList buttonListObject, List<String> messages, String userID) {
        this.buttonListObject = buttonListObject;
        this.messages = messages;
        this.userID = userID;
        parseMessages();
        preparePlainResponses();
    }


    private void parseMessages() {
        this.messages = this.messages
                .stream()
                .map(this::textToJson)
                .map(m -> "\"message\":{" + m + "}")
                .collect(Collectors.toList());

        if (buttonListObject != null) {
            this.messages.add("\"message\":{ " +
                                    "\"text\": \""+buttonListObject.getHintMessage()+"\","+
                    buttonsToJson() + "}");
        }
    }

    private void preparePlainResponses() {
        this.plainResponses = this.messages.stream()
                                .map((m -> "{\"recipient\":{\"id\":\"" + this.userID + "\"},"+ m + "}"))
                                .collect(Collectors.toList());
    }


    private String textToJson(String s) {
        return "\"text\": \"" + s + "\"";
    }

    private String buttonsToJson() {
        String res = this.buttonListObject.getButtonList()
                .stream()
                .map(b -> "{" +
                        "\"content_type\":\"text\"," +
                        "\"title\":\"" + b.getTextVisible() + "\","  +
                        "\"payload\":\"" + b.getTextHidden() + "\"," +
                        "}"
                )
                .collect(Collectors.joining(","));

        return "\"quick_replies\":[" +
                res +
                "]";
    }

    public String convertResponsesToSingleString() {
        return this.plainResponses
                .stream()
                .map(s -> s.toString())
                .collect(Collectors.joining(nL+nL));
    }

}
