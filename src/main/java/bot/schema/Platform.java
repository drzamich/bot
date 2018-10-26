package bot.schema;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Platform implements Serializable {
    private String number;
    private String mainDirection;
    private List<String> directions;
    private List<String> lines;
    private boolean isAtSipTw = false;
    private int sipTwID;

    public Platform(String number, List<String> directions, List<String> lines) {
        this.number = number;
        this.directions = directions;
        this.lines = lines;
    }

    public Platform(String number, List<String> directions) {
        this.number = number;
        this.directions = directions;
    }
}
