package bot;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "platforms")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "pkey")
public class Platform {
    @Id
    private Integer pkey;

    private int platformId;
    private int platformNumber;
    private int offline;
    private String mainDir;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="station_pkey")
    private Station station;

    @ManyToMany(mappedBy = "platforms", fetch = FetchType.LAZY)
    private List<Direction> directions;

    public Platform() {
    }
}
