package bot.externalservice.siptw.data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "stations")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "pkey")
public class Station {

    @Id
    private Integer pkey;

    private String stationName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "station", fetch = FetchType.LAZY)
    private List<AcceptedName> acceptedNames;

    @OneToMany(mappedBy = "station", fetch = FetchType.LAZY)
    private List<Platform> platforms;
}
