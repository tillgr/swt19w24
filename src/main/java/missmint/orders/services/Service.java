package missmint.orders.services;

import org.salespointframework.catalog.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Service {
	private @Id @GeneratedValue long id;

	@OneToOne
	private Product product;

	private ServiceCategory category;

	public Service() {
	}

	public Service(Product product, ServiceCategory category) {
		this.product = product;
		this.category = category;
	}

	public Product getProduct() {
		return product;
	}

	public long getId() {
		return id;
	}
}
