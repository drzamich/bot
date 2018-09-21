package bot.externalservice.apium.data;

import bot.data.Departure;
import lombok.Data;
import org.apache.commons.collections4.map.MultiValueMap;

import javax.xml.soap.Detail;
import java.io.Serializable;
import java.util.List;

@Data
public class DeparturesListWithTimes implements Serializable {
    private List<String> times;
    private MultiValueMap<String, Departure> mappedDepartures;

    public DeparturesListWithTimes(List<String> times, MultiValueMap<String, Departure> mappedDepartures) {
        this.times = times;
        this.mappedDepartures = mappedDepartures;
    }
}
