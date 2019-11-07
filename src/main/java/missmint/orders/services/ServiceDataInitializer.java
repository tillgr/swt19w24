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

	public ServiceDataInitializer(Catalog<Service> catalog) {
		this.catalog = catalog;
	}

	@Override
	public void initialize() {
		Stream.of(
			new Service("heels", Money.of(price, currency), ServiceCategory.KLUDGE),
			new Service("sole", Money.of(price, currency), ServiceCategory.KLUDGE),
			new Service("seam", Money.of(price, currency), ServiceCategory.KLUDGE),
			new Service("buttons", Money.of(price, currency), ServiceCategory.SEWING),
			new Service("seam", Money.of(price, currency), ServiceCategory.SEWING),
			new Service("patch", Money.of(price, currency), ServiceCategory.SEWING),
			new Service("copy", Money.of(price, currency), ServiceCategory.LOCKSMITH),
			new Service("engrave", Money.of(price, currency), ServiceCategory.LOCKSMITH),
			new Service("laundry", Money.of(price, currency), ServiceCategory.CLEANING),
			new Service("suits", Money.of(price, currency), ServiceCategory.CLEANING),
			new Service("leather", Money.of(price, currency), ServiceCategory.CLEANING),
			new Service("replace", Money.of(price, currency), ServiceCategory.ELECTRONICS),
			new Service("brazing", Money.of(price, currency), ServiceCategory.ELECTRONICS),
			new Service("scissors", Money.of(price, currency), ServiceCategory.GRINDERY),
			new Service("knifes", Money.of(price, currency), ServiceCategory.GRINDERY)
		).forEach(service -> {
			catalog.save(service);
		});
	}
}
