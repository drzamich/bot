package bot;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcceptedNameRepository extends CrudRepository<AcceptedName,Integer> {
    Optional<AcceptedName> findByNameAccepted(String nameAccepted);
//    AcceptedName findByNameAccepted(String nameAccepted);
}
