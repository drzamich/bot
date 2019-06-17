package bot.externalservice.ztm.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ZtmPlatform implements Serializable {

    private String number;

    private String mainDirection;

    private List<String> directions;

    private List<String> lines;

}
