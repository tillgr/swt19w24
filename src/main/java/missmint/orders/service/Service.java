package missmint.orders.service;

import org.salespointframework.catalog.Product;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

@Entity
public class Service extends Product {
	public Service() {
	}

	public Service(String name, MonetaryAmount price) {
		super(name, price);
	}
}
