package missmint.inventory.products;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Metric;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

@Entity
public class Material extends Product{

	private Material(){}

	public Material(String name, MonetaryAmount price, Metric metric){
		super(name,price,metric);
	}
}
