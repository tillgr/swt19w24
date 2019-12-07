package missmint.orders.service;

import org.salespointframework.catalog.Product;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

/**
 * A custom class the inherits <code>Product</code> with out any extension.
 *
 * @see Product
 */
@Entity
public class MissMintService extends Product {
	public MissMintService() {
	}

	public MissMintService(String name, MonetaryAmount price) {
		super(name, price);
	}
}
