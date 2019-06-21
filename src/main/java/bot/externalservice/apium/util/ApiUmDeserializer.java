package bot.externalservice.apium.util;

import bot.externalservice.apium.dto.GetTimetableResponse;
import bot.externalservice.apium.dto.GetTimetableResponseWrapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class ApiUmDeserializer extends JsonDeserializer<GetTimetableResponseWrapper> {
    @Override
    public GetTimetableResponseWrapper deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        String message = "OK";
        JsonNode result = node.get("result");
        boolean dataIsReturned = result.isArray();
        JsonNode error = node.get("error");
        if (error != null) {
            message = error.asText();
        } else if (StringUtils.isNotBlank(result.asText())) {
            message = result.asText();
        }
        if (dataIsReturned) {
            GetTimetableResponse data = node.traverse(oc).readValueAs(GetTimetableResponse.class);
            return new GetTimetableResponseWrapper(true, message, data);
        } else {
            return new GetTimetableResponseWrapper(false, message, null);
        }

    }
}
