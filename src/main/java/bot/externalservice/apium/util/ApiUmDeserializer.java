package bot.externalservice.apium.util;

import bot.externalservice.apium.dto.ApiUmResponseDto;
import bot.externalservice.apium.dto.ApiUmResponseDtoWrapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class ApiUmDeserializer extends JsonDeserializer<ApiUmResponseDtoWrapper> {
    @Override
    public ApiUmResponseDtoWrapper deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
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
            ApiUmResponseDto data = node.traverse(oc).readValueAs(ApiUmResponseDto.class);
            return new ApiUmResponseDtoWrapper(true, message, data);
        } else {
            return new ApiUmResponseDtoWrapper(false, message, null);
        }

    }
}
