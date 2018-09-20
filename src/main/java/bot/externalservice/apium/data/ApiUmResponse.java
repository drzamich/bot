package bot.externalservice.apium.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ApiUmResponse {
    @JsonProperty("result")
    private ArrayList<Map<String, ArrayList<Map<String, String>>>> result;

     public List<DepartureDetail> parseDepartureDetails(String line){
        List<DepartureDetail> res = new ArrayList<>();

        for (Map<String, ArrayList<Map<String, String>>> map : this.result) {
            String dest = map.get("values").get(3).get("value");
            String time = map.get("values").get(5).get("value");

            res.add(new DepartureDetail(time,dest,line));
        }
        return res;
    }

}

