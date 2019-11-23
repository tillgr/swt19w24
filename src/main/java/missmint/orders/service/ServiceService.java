package missmint.orders.service;

import missmint.Utils;
import missmint.orders.order.MissMintOrder;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class ServiceService {
	private final Catalog<MissMintService> catalog;

	public ServiceService(Catalog<MissMintService> catalog) {
		this.catalog = catalog;
	}

	public MissMintService getService(MissMintOrder order) {
		ProductIdentifier productIdentifier = Utils.getOrThrow(order.getOrderLines().stream().findAny()).getProductIdentifier();
		return Utils.getOrThrow(catalog.findById(productIdentifier));
	}

	public static ServiceCategory getCategory(MissMintService service) {
		Optional<String> optionalService = service.getCategories().filter(s -> !s.equals("SERVICE")).get().findAny();
		Assert.isTrue(optionalService.isPresent(), "a service should have a category");
		return ServiceCategory.valueOf(optionalService.get());
	}
}
