package missmint.inventory.products;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class OrderItem extends Product {

	private OrderItem(){}

	public OrderItem(String name, BigDecimal money) {
		super(name, Money.of(money, "EUR"));
		addCategory("ORDER_ITEM");
	}
}
