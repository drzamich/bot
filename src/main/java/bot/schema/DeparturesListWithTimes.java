package bot.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.MultiValuedMap;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class DeparturesListWithTimes implements Serializable {
    private List<String> times;
    private MultiValuedMap<String, Departure> mappedDepartures;
}
