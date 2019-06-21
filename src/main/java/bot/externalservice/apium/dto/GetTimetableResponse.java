package bot.externalservice.apium.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetTimetableResponse {

    private List<Map<String, List<Map<String, String>>>> result;
}
