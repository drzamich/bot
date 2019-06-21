package bot.externalservice.ztm.response;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ZtmPlatform implements Serializable {

    @NonNull
    private String number;

    @NonNull
    private String mainDirection;

    @NonNull
    private List<String> lines;

    @NonNull
    private String url;

    private List<String> directions;

    @NonNull
    private List<ZtmDeparture> departures;
}
