package bot;

import org.springframework.data.repository.CrudRepository;

public interface AcceptedNameRepository extends CrudRepository<AcceptedName,Integer> {
    AcceptedName findByNameAccepted(String nameAccepted);
}
