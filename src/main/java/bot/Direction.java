package bot;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "directions")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "pkey")
public class Direction {
    @Id
    private int pkey;
    private String dirName;
    private String dirNameAccepted;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="platforms_directions", joinColumns = {@JoinColumn(name="pkey_dir")},
            inverseJoinColumns = {@JoinColumn(name="pkey_platf")})
    private List<Platform> platforms;
}
