package missmint.orders.order;

import missmint.finance.service.FinanceService;
import missmint.inventory.manager.OrderItemManager;
import org.salespointframework.order.OrderManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;

/**
 * A service that handles the pick up process.
 *
 * @see missmint.orders.controllers.PickUpController
 */
@Service
public class PickUpService {
	private OrderManager<MissMintOrder> orderManager;
	private OrderService orderService;
	private final FinanceService financeService;
	private final OrderItemManager orderItemManager;

	public PickUpService(OrderManager<MissMintOrder> orderManager, OrderService orderService, FinanceService financeService, OrderItemManager orderItemManager) {
		Assert.notNull(orderManager, "orderManager should not be null");
		Assert.notNull(orderService, "orderService should not be null");
		Assert.notNull(financeService, "financeService should not be null");
		Assert.notNull(orderItemManager, "orderItemManager should not be null");

		this.orderManager = orderManager;
		this.orderService = orderService;
		this.financeService = financeService;
		this.orderItemManager = orderItemManager;
	}

	/**
	 * Changes the order state, deletes the customer's item and creates a accountancy item for the transaction.
	 *
	 * @param order The order for the item that should be picked up.
	 * @see MissMintOrder
	 */
	public void pickup(MissMintOrder order) {
		Assert.notNull(order, "order should not be null");
		Assert.isTrue(order.canPickUp(), "order must be able to be picked up");
		MonetaryAmount price = orderService.calculateCharge(order);

		String description = String.format("Additional transaction for order %s", order.getId());
		financeService.add(description, price);

		order.setOrderState(OrderState.PICKED_UP);
		orderItemManager.deleteOrderItem(order.getItem().getId());
		orderManager.save(order);
	}
}
