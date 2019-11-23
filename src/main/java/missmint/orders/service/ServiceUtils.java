package missmint.orders.service;

import org.springframework.util.Assert;

import java.util.Optional;

public class ServiceUtils {
	public static ServiceCategory getCategory(Service service) {
		Optional<String> optionalService = service.getCategories().filter(s -> !s.equals("SERVICE")).get().findAny();
		Assert.isTrue(optionalService.isPresent(), "a service should have a category");
		return ServiceCategory.valueOf(optionalService.get());
	}
}
