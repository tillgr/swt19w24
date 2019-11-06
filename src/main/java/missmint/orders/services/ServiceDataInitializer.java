package missmint.orders.services;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.DataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class ServiceDataInitializer implements DataInitializer {

	@Value("${orders.services.price}")
	private int price;
	@Value("${orders.services.currency}")
	private String currency;

	private Catalog catalog;
	private ServiceRepository serviceRepository;

	@Autowired
	public ServiceDataInitializer(Catalog catalog, ServiceRepository serviceRepository) {
		this.catalog = catalog;
		this.serviceRepository = serviceRepository;
	}

	@Override
	public void initialize() {
		Stream.of(
			new Service(new Product("heels", Money.of(price, currency)), ServiceCategory.KLUDGE),
			new Service(new Product("sole", Money.of(price, currency)), ServiceCategory.KLUDGE),
			new Service(new Product("seam", Money.of(price, currency)), ServiceCategory.KLUDGE),
			new Service(new Product("buttons", Money.of(price, currency)), ServiceCategory.SEWING),
			new Service(new Product("seam", Money.of(price, currency)), ServiceCategory.SEWING),
			new Service(new Product("patch", Money.of(price, currency)), ServiceCategory.SEWING),
			new Service(new Product("copy", Money.of(price, currency)), ServiceCategory.LOCKSMITH),
			new Service(new Product("engrave", Money.of(price, currency)), ServiceCategory.LOCKSMITH),
			new Service(new Product("laundry", Money.of(price, currency)), ServiceCategory.CLEANING),
			new Service(new Product("suits", Money.of(price, currency)), ServiceCategory.CLEANING),
			new Service(new Product("leather", Money.of(price, currency)), ServiceCategory.CLEANING),
			new Service(new Product("replace", Money.of(price, currency)), ServiceCategory.ELECTRONICS),
			new Service(new Product("brazing", Money.of(price, currency)), ServiceCategory.ELECTRONICS),
			new Service(new Product("scissors", Money.of(price, currency)), ServiceCategory.GRINDERY),
			new Service(new Product("knifes", Money.of(price, currency)), ServiceCategory.GRINDERY)
		).forEach(service -> {
			Product product = service.getProduct();
			catalog.save(product);
			serviceRepository.save(service);
		});
	}
}
