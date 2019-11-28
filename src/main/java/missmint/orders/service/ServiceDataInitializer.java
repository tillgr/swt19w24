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

	private Catalog catalog;

	public ServiceDataInitializer(Catalog catalog) {
		Assert.notNull(catalog, "catalog must not be null");

		this.catalog = catalog;
	}

	@Override
	public void initialize() {
		if (!catalog.findByAllCategories().isEmpty()) {
			return;
		}

		Stream.of(
			Pair.of(new MissMintService("kludge-heels", Money.of(price, currency)), ServiceCategory.KLUDGE.name()),
			Pair.of(new MissMintService("kludge-sole", Money.of(price, currency)), ServiceCategory.KLUDGE.name()),
			Pair.of(new MissMintService("kludge-seam", Money.of(price, currency)), ServiceCategory.KLUDGE.name()),
			Pair.of(new MissMintService("sewing-buttons", Money.of(price, currency)), ServiceCategory.SEWING.name()),
			Pair.of(new MissMintService("sewing-seam", Money.of(price, currency)), ServiceCategory.SEWING.name()),
			Pair.of(new MissMintService("sewing-patch", Money.of(price, currency)), ServiceCategory.SEWING.name()),
			Pair.of(new MissMintService("locksmith-copy", Money.of(price, currency)), ServiceCategory.LOCKSMITH.name()),
			Pair.of(new MissMintService("locksmith-engrave", Money.of(price, currency)), ServiceCategory.LOCKSMITH.name()),
			Pair.of(new MissMintService("cleaning-laundry", Money.of(price, currency)), ServiceCategory.CLEANING.name()),
			Pair.of(new MissMintService("cleaning-suits", Money.of(price, currency)), ServiceCategory.CLEANING.name()),
			Pair.of(new MissMintService("cleaning-leather", Money.of(price, currency)), ServiceCategory.CLEANING.name()),
			Pair.of(new MissMintService("electronics-replace", Money.of(price, currency)), ServiceCategory.ELECTRONICS.name()),
			Pair.of(new MissMintService("electronics-brazing", Money.of(price, currency)), ServiceCategory.ELECTRONICS.name()),
			Pair.of(new MissMintService("grindery-scissors", Money.of(price, currency)), ServiceCategory.GRINDERY.name()),
			Pair.of(new MissMintService("grindery-knifes", Money.of(price, currency)), ServiceCategory.GRINDERY.name())
		).forEach(pair -> {
			pair.getFirst().addCategory(pair.getSecond());
			pair.getFirst().addCategory("SERVICE");
			catalog.save(pair.getFirst());
		});
	}
}

