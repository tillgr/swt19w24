package missmint.inventory.repository;

import missmint.orders.service.ServiceCategory;
import missmint.orders.service.ServiceConsumption;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ServiceConsumptionRepo extends CrudRepository<ServiceConsumption, Long> {

	public Optional<ServiceConsumption> findServiceConsumptionByServiceCategory(ServiceCategory serviceCategory);
}
