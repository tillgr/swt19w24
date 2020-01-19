package missmint.orders.service;

import missmint.Utils;
import missmint.orders.order.MissMintOrder;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Provides database related utils for services.
 *
 * @see MissMintService
 */
@Service
public class ServiceManager {
	private Catalog<MissMintService> catalog;

	public ServiceManager(Catalog<MissMintService> catalog) {
		Assert.notNull(catalog, "catalog should not be null");

		this.catalog = catalog;
	}

	/**
	 * Returns the service's category.
	 *
	 * @param service The service to return the category for.
	 * @return The service's category.
	 * @see ServiceCategory
	 */
	public static ServiceCategory getCategory(MissMintService service) {
		Assert.notNull(service, "service should not be null");

		Optional<String> optionalService = service.getCategories().filter(s -> !s.equals("SERVICE")).get().findAny();
		Assert.isTrue(optionalService.isPresent(), "a service should have a category");
		return ServiceCategory.valueOf(optionalService.get());
	}

	/**
	 * @param order The order to find the service of.
	 * @return The service of an order.
	 */
	public MissMintService getService(MissMintOrder order) {
		Assert.notNull(order, "order should not be null");

		ProductIdentifier productIdentifier = Utils.getOrThrow(
				order.getOrderLines().stream().findAny()
		).getProductIdentifier();
		return Utils.getOrThrow(catalog.findById(productIdentifier));
	}

	/**
	 * Find all services in the database.
	 *
	 * @return all services
	 */
	public Streamable<MissMintService> findAll() {
		return catalog.findByAllCategories("SERVICE");
	}

	/**
	 * Returns a service with this id.
	 *
	 * @param id The service id
	 * @return the service if found
	 * @see Catalog#findById
	 */
	public Optional<MissMintService> findById(ProductIdentifier id) {
		return catalog.findById(id);
	}
}
