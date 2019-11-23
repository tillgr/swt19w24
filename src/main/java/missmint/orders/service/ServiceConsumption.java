package missmint.orders.service;

import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Map;

@Entity
public class ServiceConsumption {
	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.STRING)
	private ServiceCategory serviceCategory;

	@ElementCollection
	private Map<ProductIdentifier, Integer> materialCostMap;

	public ServiceConsumption() {}

	public ServiceConsumption(ServiceCategory serviceCategory, Map<ProductIdentifier, Integer> materialCostMap) {

		Assert.notNull(serviceCategory, "ServiceCategory must not be null");
		Assert.notNull(materialCostMap, "Map must not be null");

		this.serviceCategory = serviceCategory;
		this.materialCostMap = materialCostMap;
	}

	public Long getId() {
		return id;
	}

	public ServiceCategory getServiceCategory() {
		return serviceCategory;
	}

	public Map<ProductIdentifier, Integer> getMaterialCostMap() {
		return materialCostMap;
	}
}
