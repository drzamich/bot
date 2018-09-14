package bot;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name ="station_names_accepted")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "pkey")
public class AcceptedName {

    @Id
    @Column(name="pkey")
    private Integer pkey;

    @Column(name="name_accepted")
    private String nameAccepted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="station_name_pkey")
    private Station station;

    public AcceptedName() {
    }

}
