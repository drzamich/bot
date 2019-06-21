package bot.schema.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StationDto {
    private int id;
    private boolean atSipTw;
    private List<PlatformDto> platforms;
}
