package missmint.orders.service;

import missmint.inventory.repository.ServiceConsumptionRepo;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class ServiceConsumptionManager {

	private ServiceConsumptionRepo repo;

	public ServiceConsumptionManager(ServiceConsumptionRepo repo) {
		Assert.notNull(repo, "Repository must not be null.");
		this.repo = repo;
	}

	public ServiceConsumption save(ServiceConsumption serviceConsumption) {
		return repo.save(serviceConsumption);
	}

	public Iterable<ServiceConsumption> findAll() {
		return repo.findAll();
	}

	public Optional<ServiceConsumption> findConsumptionByCategory(ServiceCategory category) {
		return repo.findServiceConsumptionByServiceCategory(category);
	}
}
