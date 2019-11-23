package missmint.inventory.products;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Metric;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

@Entity
public class OrderItem extends Product {

	private OrderItem(){}

	public OrderItem(String name, MonetaryAmount price, Metric metric){
		super(name,price,metric);
	}

}
