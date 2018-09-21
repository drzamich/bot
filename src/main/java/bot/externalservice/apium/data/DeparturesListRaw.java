package bot.externalservice.apium.data;

import lombok.Data;
import org.apache.commons.collections4.map.MultiValueMap;

import java.io.Serializable;
import java.util.List;

@Data
public class DeparturesListRaw implements Serializable {
    private List<String> times;
    private MultiValueMap<String, DepartureDetail> mappedDepartures;

    public DeparturesListRaw(List<String> times, MultiValueMap<String, DepartureDetail> mappedDepartures) {
        this.times = times;
        this.mappedDepartures = mappedDepartures;
    }
}
