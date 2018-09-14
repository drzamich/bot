package bot.data;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Departure {
    @SerializedName("Linea")
    private String line;

    @SerializedName("Destino")
    private String destination;

    @SerializedName("TiempoLlegada")
    private String timeExact;

    @SerializedName("Minutos")
    private String timeMinutes;

    @SerializedName("Propiedades")
    private String properties;
}
