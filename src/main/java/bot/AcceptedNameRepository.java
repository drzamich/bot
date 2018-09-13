package bot;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcceptedNameRepository extends CrudRepository<AcceptedName,Integer> {
    AcceptedName findByNameAccepted(String nameAccepted);
}
