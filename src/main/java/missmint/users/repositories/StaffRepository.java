package missmint.users.repositories;

import missmint.orders.service.ServiceCategory;
import missmint.users.model.Staff;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;

/**
 * Repository for saving staff members on disk
 */
public interface StaffRepository extends CrudRepository<Staff, Long> {
	boolean existsBySkillsContaining(ServiceCategory skill);
	Streamable<Staff> findAllBySkillsContaining(ServiceCategory skill);
	Optional<Staff> findByUserAccount(UserAccount userAccount);
}
