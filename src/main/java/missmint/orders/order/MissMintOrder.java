package missmint.orders.order;

import missmint.inventory.products.OrderItem;
import missmint.orders.service.MissMintService;
import org.salespointframework.order.Order;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
public class MissMintOrder extends Order {
	private String customer;
	private LocalDate inbound;
	private LocalDate expectedFinished;
	private LocalDate finished;
	private OrderState orderState;
	@OneToOne
	private OrderItem item;

	public MissMintOrder() {
	}

	public MissMintOrder(UserAccount userAccount, String customer, LocalDate inbound, LocalDate expectedFinished, MissMintService service, OrderItem item) {
		super(userAccount);

		Assert.notNull(customer, "customer must not be null");
		Assert.notNull(inbound, "inbound must not be null");
		Assert.notNull(expectedFinished, "expectedFinished must not be null");
		Assert.notNull(service, "service must not be null");
		Assert.notNull(item, "item must not be null");

		this.customer = customer;
		this.inbound = inbound;
		this.expectedFinished = expectedFinished;
		this.orderState = OrderState.WAITING;
		this.item = item;
		addOrderLine(service, Quantity.of(1));
	}

	public LocalDate getLatestFinished() {
		Assert.notNull(finished, "finished must not be null");

		return expectedFinished.isAfter(finished) ? expectedFinished : finished;
	}

	public boolean canPickUp() {
		return orderState == OrderState.FINISHED || orderState == OrderState.STORED;
	}

	public String getCustomer() {
		return customer;
	}

	public LocalDate getInbound() {
		return inbound;
	}

	public LocalDate getExpectedFinished() {
		return expectedFinished;
	}

	public LocalDate getFinished() {
		return finished;
	}

	public OrderState getOrderState() {
		return orderState;
	}

	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
	}

	public void setFinished(LocalDate finished) {
		this.finished = finished;
	}

	public OrderItem getItem() {
		return item;
	}
}
