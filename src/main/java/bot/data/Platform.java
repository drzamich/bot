package bot.data;

import lombok.Data;

@Data
public class Platform {
    private String number;
    private String direction;

    public Platform(String number, String direction) {
        this.number = number;
        this.direction = direction;
    }
}
