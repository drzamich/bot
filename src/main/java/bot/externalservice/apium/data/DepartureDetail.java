package bot.externalservice.apium.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepartureDetail implements Serializable {
    private String time;
    private String direction;
    private String line;

    public DepartureDetail(String time, String direction,String line) {
        this.time = time;
        this.direction = direction;
        this.line = line;
    }
}
