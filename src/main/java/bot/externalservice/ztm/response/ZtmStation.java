package bot.externalservice.ztm.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ZtmStation {

    private String id;

    private String mainName;

    private String url;

    private List<ZtmPlatform> platforms;

}
