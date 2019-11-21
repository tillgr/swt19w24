package missmint.users.repositories;

import missmint.orders.service.Service;
import missmint.users.model.Staff;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface StaffRepository extends CrudRepository<Staff, Long> {
	public long countBySkillsContains(Set<Service> skills);
}
