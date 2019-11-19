package missmint.orders.order;

import missmint.orders.service.Service;
import org.salespointframework.order.Order;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class MissMintOrder extends Order {
	private String customer;
	private LocalDate inbound;
	private LocalDate expectedFinished;
	private LocalDate finished;
	private OrderState orderState;

	public MissMintOrder() {
	}

	public MissMintOrder(UserAccount userAccount, String customer, LocalDate inbound, LocalDate expectedFinished, Service service) {
		super(userAccount);

		Assert.notNull(customer, "customer must not be null");
		Assert.notNull(inbound, "inbound must not be null");
		Assert.notNull(expectedFinished, "expectedFinished must not be null");
		Assert.notNull(service, "service must not be null");

		this.customer = customer;
		this.inbound = inbound;
		this.expectedFinished = expectedFinished;
		this.orderState = OrderState.WAITING;
		addOrderLine(service, Quantity.of(1));
	}

	public LocalDate getLatestFinished() {
		Assert.notNull(finished, "finished must not be null");

		return expectedFinished.isAfter(finished) ? expectedFinished : finished;
	}

	public boolean canPickUp() {
		return orderState == OrderState.FINISHED || orderState == OrderState.STORED;
	}
}
