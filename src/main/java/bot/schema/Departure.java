package bot.schema;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Departure implements Serializable {

    private String line;
    private String direction;
    private String time;

}
