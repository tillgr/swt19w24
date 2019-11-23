package missmint.orders.service;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceManager {
	private Catalog<missmint.orders.service.Service> catalog;

	public ServiceManager(Catalog<missmint.orders.service.Service> catalog) {
		this.catalog = catalog;
	}

	public Streamable findAll() {
		return catalog.findByAllCategories("SERVICE");
	}

	public Optional<missmint.orders.service.Service> findById(ProductIdentifier id) {
		return catalog.findById(id);
	}
}
