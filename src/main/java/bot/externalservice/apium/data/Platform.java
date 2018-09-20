package bot.externalservice.apium.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Platform implements Serializable {
    private String number;
    private String direction;
    private List<String> lines;

    public Platform(String number, String direction, List<String> lines) {
        this.number = number;
        this.direction = direction;
        this.lines = lines;
    }
}