package bot.schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class JSONResponse {
    private List<Button> buttonList;
    private List<String> messages;
    private List<String> responses;

    public JSONResponse(List<Button> buttonList, List<String> messages) {
        this.buttonList = buttonList;
        this.messages = messages;
        generateResponses();
    }



    private void generateResponses() {
        this.responses = this.messages
                        .stream()
                        .map(this::textToJson)
                        .map(m-> "\"message\":{"+m+"}")
                        .collect(Collectors.toList());

        if(buttonList != null){
            this.responses.add("\"message\":{"+buttonsToJson()+"}");
        }
    }

    private String textToJson(String s){
        return "\"text\": \""+ s + "\"";
    }

    private String buttonsToJson(){
        String res = this.buttonList
                            .stream()
                                    .map( b -> "{\n"+
                                                "\"content_type\":\"text\",\n"+
                                                "\"title\":\""+b.getTextVisible()+"\",\n"+
                                                "\"payload\":\""+b.getTextHidden()+"\",\n"+
                                                "}"
                                    )
                            .collect(Collectors.joining(",\n"));
        return "\"quick_replies\":[\n"+
                res+"\n"+
                "]";
    }

    public String convertResponsesToSingleString() {
        return this.responses
                .stream()
                .collect(Collectors.joining("\n\n"));
    }

}
