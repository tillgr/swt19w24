package missmint.inventory.products;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Metric;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

@Entity
public class orderItem extends Product {

	private orderItem(){}

	public orderItem(String name, MonetaryAmount price, Metric metric){
		super(name,price,metric);
	}

}
