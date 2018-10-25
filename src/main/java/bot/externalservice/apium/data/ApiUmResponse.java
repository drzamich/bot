package bot.externalservice.apium.data;

import bot.schema.Departure;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ApiUmResponse {
    @JsonProperty("result")
    private ArrayList<Map<String, ArrayList<Map<String, String>>>> result;

     public List<Departure> parseDepartureDetails(String line){
        List<Departure> res = new ArrayList<>();

        for (Map<String, ArrayList<Map<String, String>>> map : this.result) {
            String dest = map.get("values").get(3).get("value");
            String time = map.get("values").get(5).get("value");

            res.add(new Departure(line,dest,time));
        }
        return res;
    }

}

