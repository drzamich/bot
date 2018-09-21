package bot.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class Departure implements Serializable {
    private String line;
    private String direction;
    private String time;

    public Departure(String line, String direction, String time) {
        this.line = line;
        this.direction = direction;
        this.time = time;
    }
}
