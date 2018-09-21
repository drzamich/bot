package bot.data;

import lombok.Data;

@Data
public class Departure {
    private String line;
    private String direction;
    private String time;

    public Departure(String line, String direction, String time) {
        this.line = line;
        this.direction = direction;
        this.time = time;
    }
}
