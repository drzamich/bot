package bot.schema.response;

import java.util.List;

public interface Response {

    String getMessage();

    List<String> getQuickRepliesTexts();
}

