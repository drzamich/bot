package bot.schema.response;

import java.util.Collections;
import java.util.List;

public class SimpleErrorResponse implements Response {
    private String message;

    public SimpleErrorResponse(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<String> getQuickRepliesTexts() {
        return Collections.emptyList();
    }
}
