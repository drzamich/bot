package bot.schema.response;

import bot.schema.dto.StationDto;

import java.util.List;

public class ChoosePlatformResponse implements Response {

    public ChoosePlatformResponse(StationDto stationDto) {
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public List<String> getQuickRepliesTexts() {
        return null;
    }
}
