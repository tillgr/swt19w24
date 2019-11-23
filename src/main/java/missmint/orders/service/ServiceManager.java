package missmint.orders.service;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceManager {
	private Catalog<MissMintService> catalog;

	public ServiceManager(Catalog<MissMintService> catalog) {
		this.catalog = catalog;
	}

	public Streamable findAll() {
		return catalog.findByAllCategories("SERVICE");
	}

	public Optional<MissMintService> findById(ProductIdentifier id) {
		return catalog.findById(id);
	}
}
