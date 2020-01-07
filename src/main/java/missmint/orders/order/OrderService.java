package missmint.orders.order;

import missmint.inventory.products.OrderItem;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceManager;
import missmint.rooms.RoomRepository;
import missmint.time.TimeTableEntry;
import missmint.users.repositories.StaffRepository;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static missmint.orders.order.OrderState.*;

/**
 * This class provides services for handling orders.
 *
 * @see MissMintOrder
 */
@Service
public class OrderService {
	private final BusinessTime businessTime;
	private final RoomRepository rooms;
	private final StaffRepository staffRepository;
	private OrderManager<MissMintOrder> orderManager;
	private Catalog<OrderItem> itemCatalog;

	@Value("${general.currency}")
	private String currency;
	@Value("${orders.pickup.compensation.percent}")
	private long compensation;

	public OrderService(BusinessTime businessTime, RoomRepository rooms, StaffRepository staffRepository, OrderManager<MissMintOrder> orderManager, Catalog<OrderItem> itemCatalog) {
		Assert.notNull(businessTime, "businessTime should not be null");
		Assert.notNull(rooms, "rooms should not be null");
		Assert.notNull(staffRepository, "staffRepository should not be null");
		Assert.notNull(orderManager, "orderManager should not be null");
		Assert.notNull(itemCatalog, "itemCatalog should not be null");

		this.businessTime = businessTime;
		this.rooms = rooms;
		this.staffRepository = staffRepository;
		this.orderManager = orderManager;
		this.itemCatalog = itemCatalog;
	}

	/**
	 * Calculates the amount the employee needs to charge the customer.
	 * <p>
	 * This method considers the time the item was stored and the number of days the item was not finished through promised.
	 *
	 * @param order The order to calculate the charge for.
	 * @return A (possible negative or zero) charge.
	 * @see MissMintOrder
	 */
	public MonetaryAmount calculateCharge(MissMintOrder order) {
		Assert.notNull(order, "order must not be null");
		Assert.isTrue(order.canPickUp(), "order can not be picked up");

		long daysTooLate = ChronoUnit.DAYS.between(order.getExpectedFinished(), order.getFinished());
		var deduction = BigDecimal.valueOf(compensation, 2).multiply(BigDecimal.valueOf(Math.min(Math.max(daysTooLate, 0), 10)));

		MonetaryAmount charge = order.getTotal().multiply(deduction).negate();

		if (order.getOrderState() == OrderState.FINISHED) {
			return charge;
		}

		long weeksStored = ChronoUnit.WEEKS.between(order.getLatestFinished(), businessTime.getTime().toLocalDate());

		Assert.isTrue(weeksStored > 0, "order has to be in the shop for a week or more to be stored");
		return charge.add(Money.of(BigDecimal.valueOf(5, 1), currency).multiply(weeksStored));
	}

	/**
	 * Checks if the store can currently accept an order with this service.
	 * <p>
	 * This method checks for the availability of a room and a skilled staff.
	 *
	 * @param service The service to check for.
	 * @return true if the order is acceptable
	 */
	public boolean isOrderAcceptable(MissMintService service) {
		Assert.notNull(service, "service should not be null");

		return rooms.count() > 0 && staffRepository.existsBySkillsContaining(ServiceManager.getCategory(service));
	}

	/**
	 * Updates all orders until the orders change no longer.
	 */
	public void updateOrders() {
		boolean updated;
		do {
			updated = updateOrdersOnce();
		} while (updated);
	}

	/**
	 * Updates every order once.
	 * <p>
	 * Changes orders from waiting to processing when the repair slot is passing.
	 * Changes from in progress to finished when the repair slot is over.
	 * Changes from finished to stored when the order is finished for more than a week.
	 * Changes from stored to charitable used when the order is stored for three months.
	 *
	 * @return true if at least on order is updated
	 * @see OrderState
	 */
	private boolean updateOrdersOnce() {
		AtomicBoolean changed = new AtomicBoolean(false);

		orderManager.findAll(Pageable.unpaged()).forEach(order -> {
			TimeTableEntry entry = order.getEntry();

			switch (order.getOrderState()) {
				case WAITING:
					if (entry != null && businessTime.getTime().isAfter(entry.getBeginning())) {
						order.setOrderState(IN_PROGRESS);
						changed.set(true);
					}
					break;
				case IN_PROGRESS:
					if (entry != null && businessTime.getTime().isAfter(entry.getEnd())) {
						order.setOrderState(FINISHED);
						order.setFinished(entry.getDate());
						changed.set(true);
					}
					break;
				case FINISHED:
					if (order.getLatestFinished().plusWeeks(1).isBefore(businessTime.getTime().toLocalDate())) {
						order.setOrderState(OrderState.STORED);
						changed.set(true);
					}
					break;
				case STORED:
					if (order.getLatestFinished().plusWeeks(1).plusMonths(3).isBefore(businessTime.getTime().toLocalDate())) {
						order.setOrderState(OrderState.CHARITABLE_USED);
						OrderItem item = order.getItem();
						order.setItem(null);
						orderManager.save(order);
						itemCatalog.delete(item);
						changed.set(true);
					}
					break;
				default:
					break;
			}
			orderManager.save(order);
		});

		return changed.get();
	}

}
