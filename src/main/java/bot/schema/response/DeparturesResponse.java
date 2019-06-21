package bot.schema.response;

import bot.schema.dto.DepartureDto;

import java.util.List;

public class DeparturesResponse implements Response {
    public DeparturesResponse(List<DepartureDto> departureDtos) {

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
