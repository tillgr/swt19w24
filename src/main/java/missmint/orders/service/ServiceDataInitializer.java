package missmint.orders.service;

import com.mysema.commons.lang.Assert;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.DataInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@Order(10)
public class ServiceDataInitializer implements DataInitializer {

	@Value("${orders.services.price}")
	private int price;
	@Value("${general.currency}")
	private String currency;

	private Catalog<Service> catalog;

	public ServiceDataInitializer(Catalog<Service> catalog) {
		Assert.notNull(catalog, "catalog must not be null");

		this.catalog = catalog;
	}

	@Override
	public void initialize() {
		if (!catalog.findByAllCategories().isEmpty()) {
			return;
		}

		Stream.of(
			Pair.of(new Service("kludge-heels", Money.of(price, currency)), ServiceCategory.KLUDGE.name()),
			Pair.of(new Service("kludge-sole", Money.of(price, currency)), ServiceCategory.KLUDGE.name()),
			Pair.of(new Service("kludge-seam", Money.of(price, currency)), ServiceCategory.KLUDGE.name()),
			Pair.of(new Service("sewing-buttons", Money.of(price, currency)), ServiceCategory.SEWING.name()),
			Pair.of(new Service("sewing-seam", Money.of(price, currency)), ServiceCategory.SEWING.name()),
			Pair.of(new Service("sewing-patch", Money.of(price, currency)), ServiceCategory.SEWING.name()),
			Pair.of(new Service("locksmith-copy", Money.of(price, currency)), ServiceCategory.LOCKSMITH.name()),
			Pair.of(new Service("locksmith-engrave", Money.of(price, currency)), ServiceCategory.LOCKSMITH.name()),
			Pair.of(new Service("cleaning-laundry", Money.of(price, currency)), ServiceCategory.CLEANING.name()),
			Pair.of(new Service("cleaning-suits", Money.of(price, currency)), ServiceCategory.CLEANING.name()),
			Pair.of(new Service("cleaning-leather", Money.of(price, currency)), ServiceCategory.CLEANING.name()),
			Pair.of(new Service("electronics-replace", Money.of(price, currency)), ServiceCategory.ELECTRONICS.name()),
			Pair.of(new Service("electronics-brazing", Money.of(price, currency)), ServiceCategory.ELECTRONICS.name()),
			Pair.of(new Service("grindery-scissors", Money.of(price, currency)), ServiceCategory.GRINDERY.name()),
			Pair.of(new Service("grindery-knifes", Money.of(price, currency)), ServiceCategory.GRINDERY.name())
		).forEach(pair -> {
			pair.getFirst().addCategory(pair.getSecond());
			catalog.save(pair.getFirst());
		});
	}
}

