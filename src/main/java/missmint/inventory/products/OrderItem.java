package missmint.inventory.products;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import javax.persistence.Entity;

@Entity
public class OrderItem extends Product {

	private OrderItem(){}

	public OrderItem(String name) {
		super(name, Money.of(0, "USD"));
		addCategory("ORDER_ITEM");
	}
}
