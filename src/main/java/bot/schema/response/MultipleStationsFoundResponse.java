package bot.schema.response;

import bot.schema.dto.StationDto;

import java.util.List;

public class MultipleStationsFoundResponse implements Response {

    public MultipleStationsFoundResponse(List<StationDto> matchingStations) {

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
