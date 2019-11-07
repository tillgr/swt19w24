package missmint.orders.services;

import org.salespointframework.catalog.Product;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Service extends Product {
	private ServiceCategory category;

	public Service() {
	}

	public Service(String name, MonetaryAmount price, ServiceCategory category) {
		super(name, price);
		this.category = category;
	}
}
