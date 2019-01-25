package bot.externalservice.siptw.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;

@Data
public class PlatformSipTw implements Serializable {
    @JsonProperty("IDParada")
    private int id;

    @JsonProperty("Nombre")
    private String name;

    @JsonProperty("Categoria")
    private int category;

    @JsonProperty("Codigo")
    private String innerId;

    @JsonProperty("Latitud")
    private float latitude;

    @JsonProperty("Longitud")
    private float longitude;

    @JsonProperty("NombreConDestino")
    private String destination;
}
