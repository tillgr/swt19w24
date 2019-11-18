package missmint.orders.order;

import missmint.orders.service.Service;
import org.salespointframework.order.Order;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;

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
		this.customer = customer;
		this.inbound = inbound;
		this.expectedFinished = expectedFinished;
		this.orderState = OrderState.WAITING;
		addOrderLine(service, Quantity.of(1));
	}

	public LocalDate getLatestFinished() {
		Objects.requireNonNull(getExpectedFinished());
		return getExpectedFinished().isAfter(getFinished()) ? getExpectedFinished() : getFinished();
	}

	public boolean canPickUp() {
		return getOrderState() == OrderState.FINISHED || getOrderState() == OrderState.STORED;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public LocalDate getInbound() {
		return inbound;
	}

	public void setInbound(LocalDate inbound) {
		this.inbound = inbound;
	}

	public LocalDate getExpectedFinished() {
		return expectedFinished;
	}

	public void setExpectedFinished(LocalDate expectedFinished) {
		this.expectedFinished = expectedFinished;
	}

	public LocalDate getFinished() {
		return finished;
	}

	public void setFinished(LocalDate finished) {
		this.finished = finished;
	}

	public OrderState getOrderState() {
		return orderState;
	}

	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
	}
}
