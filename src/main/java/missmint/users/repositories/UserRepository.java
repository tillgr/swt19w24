package missmint.users.repositories;

import missmint.users.model.Staff;
import org.springframework.data.repository.CrudRepository;

/**
 * A repository interface to manage user instances
 */
public interface UserRepository extends CrudRepository<Staff, Long> {
}
