package missmint.users.repositories;

import missmint.orders.service.ServiceCategory;
import missmint.users.model.Staff;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface StaffRepository extends CrudRepository<Staff, Long> {
	boolean existsBySkillsContaining(ServiceCategory skill);
	Streamable<Staff> findAllBySkillsContaining(ServiceCategory skill);
}
