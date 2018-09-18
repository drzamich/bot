package bot.externalservice.siptw.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Departure {
    @JsonProperty("Linea")
    private String line;

    @JsonProperty("Destino")
    private String destination;

    @JsonProperty("TiempoLlegada")
    private String timeExact;

    @JsonProperty("Minutos")
    private String timeMinutes;

    @JsonProperty("Propiedades")
    private String properties;

}
