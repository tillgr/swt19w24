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

import static missmint.orders.order.OrderState.FINISHED;
import static missmint.orders.order.OrderState.IN_PROGRESS;

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
		this.businessTime = businessTime;
		this.rooms = rooms;
		this.staffRepository = staffRepository;
		this.orderManager = orderManager;
		this.itemCatalog = itemCatalog;
	}

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

	public boolean isOrderAcceptable(MissMintService service) {
		return rooms.count() > 0 && staffRepository.existsBySkillsContaining(ServiceManager.getCategory(service));
	}

	public void updateOrders() {
		boolean updated;
		do {
			updated = updateOrdersOnce();
		} while (updated);
	}

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
