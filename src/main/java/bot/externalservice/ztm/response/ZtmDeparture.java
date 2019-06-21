package bot.externalservice.ztm.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ZtmDeparture {

    private LocalDateTime time;

    private String line;

    private String direction;
}
