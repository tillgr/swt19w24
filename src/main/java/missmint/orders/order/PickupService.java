package missmint.orders.order;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.order.OrderManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;

@Service
public class PickupService {
	private OrderManager<MissMintOrder> orderManager;
	private OrderService orderService;
	private Accountancy accountancy;

	public PickupService(OrderManager<MissMintOrder> orderManager, OrderService orderService, Accountancy accountancy) {
		this.orderManager = orderManager;
		this.orderService = orderService;
		this.accountancy = accountancy;
	}

	public void pickup(MissMintOrder order) {
		Assert.isTrue(order.canPickUp(), "order must be able to be picked up");
		MonetaryAmount price = orderService.calculateCharge(order);
		if (!price.isZero()) {
			accountancy.add(new AccountancyEntry(price, String.format("Additional transaction for order %s", order.getId())));
		}
		order.setOrderState(OrderState.PICKED_UP);
		orderManager.save(order);
	}
}
