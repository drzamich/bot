package bot.data;


import lombok.Data;

import java.util.List;

@Data
public class Station {
    private String name;
    private List<Platform> platforms;

    public Station(String name) {
        this.name = name;
    }

    public Station(String name, List<Platform> platforms) {
        this.name = name;
        this.platforms = platforms;
    }
}
