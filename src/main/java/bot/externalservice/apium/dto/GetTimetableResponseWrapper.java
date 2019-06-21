package bot.externalservice.apium.dto;

import bot.externalservice.apium.util.ApiUmDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonDeserialize(using = ApiUmDeserializer.class)
public class GetTimetableResponseWrapper {

    private boolean success;

    private String message;

    private GetTimetableResponse data;

}

