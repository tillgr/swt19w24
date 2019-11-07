package missmint.orders.order;

import missmint.orders.services.Service;
import org.salespointframework.order.Order;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
public class MissMintOrder extends Order {
	private String customer;
	private LocalDate inbound;
	private LocalDate expectedFinished;
	private LocalDate finished;

	public MissMintOrder() {
	}

	public MissMintOrder(UserAccount userAccount, String customer, LocalDate inbound, Service service) {
		super(userAccount);
		this.customer = customer;
		this.inbound = inbound;
		addOrderLine(service, Quantity.of(1));
	}
}
