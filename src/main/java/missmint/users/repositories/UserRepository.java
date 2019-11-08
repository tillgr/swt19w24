package missmint.users.repositories;

import missmint.users.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * A repository interface to manage user instances
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
