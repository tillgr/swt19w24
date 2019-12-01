package missmint.orders.order;

import missmint.finance.FinanceService;
import missmint.inventory.manager.OrderItemManager;
import org.salespointframework.order.OrderManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;

@Service
public class PickupService {
	private OrderManager<MissMintOrder> orderManager;
	private OrderService orderService;
	private final FinanceService financeService;
	private final OrderItemManager orderItemManager;

	public PickupService(OrderManager<MissMintOrder> orderManager, OrderService orderService, FinanceService financeService, OrderItemManager orderItemManager) {
		this.orderManager = orderManager;
		this.orderService = orderService;
		this.financeService = financeService;
		this.orderItemManager = orderItemManager;
	}

	public void pickup(MissMintOrder order) {
		Assert.isTrue(order.canPickUp(), "order must be able to be picked up");
		MonetaryAmount price = orderService.calculateCharge(order);
		if (!price.isZero()) {
			String description = String.format("Additional transaction for order %s", order.getId());
			financeService.add(description, price);
		}
		order.setOrderState(OrderState.PICKED_UP);
		orderItemManager.deleteOrderItem(order.getItem().getId());
		orderManager.save(order);

	}
}
