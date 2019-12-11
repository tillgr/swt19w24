package missmint.orders.order;

import missmint.inventory.products.OrderItem;
import missmint.orders.service.MissMintService;
import missmint.time.TimeTableEntry;
import org.salespointframework.order.Order;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.util.Assert;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDate;

/**
 * The order describing the repair request of a customer.
 */
@Entity
public class MissMintOrder extends Order {
	private String customer;
	private LocalDate inbound;
	private LocalDate expectedFinished;
	private LocalDate finished;
	private OrderState orderState;

	@OneToOne(cascade = CascadeType.ALL)
	private OrderItem item;

	@OneToOne(mappedBy = "order")
	private TimeTableEntry entry;

	public MissMintOrder() {
	}

	/**
	 * Create a new order.
	 *
	 * @param userAccount The user that created the order.
	 * @param customer The customer that requested the repair.
	 * @param inbound The inbound date.
	 * @param service The service this order is related to.
	 * @param item A description for the customer's item.
	 * @see UserAccount
	 * @see MissMintService
	 * @see OrderItem
	 */
	public MissMintOrder(UserAccount userAccount, String customer, LocalDate inbound, MissMintService service, OrderItem item) {
		super(userAccount);

		Assert.notNull(customer, "customer must not be null");
		Assert.notNull(inbound, "inbound must not be null");
		Assert.notNull(service, "service must not be null");
		Assert.notNull(item, "item must not be null");

		this.customer = customer;
		this.inbound = inbound;
		this.orderState = OrderState.WAITING;
		this.item = item;
		addOrderLine(service, Quantity.of(1));
	}

	/**
	 * @return The latest of the finishing or the expected finishing date.
	 */
	public LocalDate getLatestFinished() {
		Assert.notNull(finished, "finished must not be null");
		Assert.notNull(expectedFinished, "expectedFinished must not be null");

		return expectedFinished.isAfter(finished) ? expectedFinished : finished;
	}

	/**
	 * @return true if the order can be picked up by the customer
	 * @see OrderState
	 */
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

	public void setExpectedFinished(LocalDate expectedFinished) {
		this.expectedFinished = expectedFinished;
	}

	public TimeTableEntry getEntry() {
		return entry;
	}

	public void setItem(OrderItem item) {
		this.item = item;
	}

	public void setInbound(LocalDate inbound) {
		this.inbound = inbound;
	}

	public void setEntry(TimeTableEntry entry) {
		this.entry = entry;
	}
}
