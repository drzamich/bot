package bot.externalservice.siptw.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class TextHtmlConverter extends AbstractJackson2HttpMessageConverter {
    public TextHtmlConverter() {
        super(new ObjectMapper(), MediaType.TEXT_HTML);
    }
}

