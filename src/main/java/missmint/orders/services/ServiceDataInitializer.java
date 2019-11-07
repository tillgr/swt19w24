package missmint.orders.services;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.DataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class ServiceDataInitializer implements DataInitializer {

	@Value("${orders.services.price}")
	private int price;
	@Value("${orders.services.currency}")
	private String currency;

	private Catalog<Product> catalog;

	public ServiceDataInitializer(Catalog<Product> catalog) {
		this.catalog = catalog;
	}

	@Override
	public void initialize() {
		Stream.of(
			Pair.of(new Product("heels", Money.of(price, currency)), "KLUDGE"),
			Pair.of(new Product("sole", Money.of(price, currency)), "KLUDGE"),
			Pair.of(new Product("seam", Money.of(price, currency)), "KLUDGE"),
			Pair.of(new Product("buttons", Money.of(price, currency)), "SEWING"),
			Pair.of(new Product("seam", Money.of(price, currency)), "SEWING"),
			Pair.of(new Product("patch", Money.of(price, currency)), "SEWING"),
			Pair.of(new Product("copy", Money.of(price, currency)), "LOCKSMITH"),
			Pair.of(new Product("engrave", Money.of(price, currency)), "LOCKSMITH"),
			Pair.of(new Product("laundry", Money.of(price, currency)), "CLEANING"),
			Pair.of(new Product("suits", Money.of(price, currency)), "CLEANING"),
			Pair.of(new Product("leather", Money.of(price, currency)), "CLEANING"),
			Pair.of(new Product("replace", Money.of(price, currency)), "ELECTRONICS"),
			Pair.of(new Product("brazing", Money.of(price, currency)), "ELECTRONICS"),
			Pair.of(new Product("scissors", Money.of(price, currency)), "GRINDERY"),
			Pair.of(new Product("knifes", Money.of(price, currency)), "GRINDERY")
		).forEach(pair -> {
			pair.getFirst().addCategory(pair.getSecond());
			catalog.save(pair.getFirst());
		});
	}
}
