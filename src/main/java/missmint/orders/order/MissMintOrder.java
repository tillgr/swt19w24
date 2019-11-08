package missmint.orders.order;

import org.salespointframework.catalog.Product;
import org.salespointframework.order.Order;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class MissMintOrder extends Order {
	private String customer;
	private LocalDate inbound;
	private LocalDate expectedFinished;
	private LocalDate finished;

	public MissMintOrder() {
	}

	public MissMintOrder(UserAccount userAccount, String customer, LocalDate inbound, Product service) {
		super(userAccount);
		this.customer = customer;
		this.inbound = inbound;
		addOrderLine(service, Quantity.of(1));
	}

	public LocalDate getInbound() {
		return inbound;
	}

	public String getCustomer() {
		return customer;
	}
}
