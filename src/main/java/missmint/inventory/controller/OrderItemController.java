package missmint.inventory.controller;

import missmint.inventory.manager.OrderItemManager;
import missmint.orders.order.MissMintOrder;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderItemController {

	private final OrderItemManager orderItemManager;
	private final OrderManager<MissMintOrder> orderManager;

	public OrderItemController(OrderItemManager orderItemManager, OrderManager<MissMintOrder> orderManager) {
		this.orderItemManager = orderItemManager;
		this.orderManager = orderManager;
	}

	/**
	 * Lists all Order-Items in the system.
	 *
	 * @return The orders template.
	 */
	@GetMapping("/orderItem")
	@PreAuthorize("isAuthenticated()")
	public String orderItem(Model model) {
		Page<MissMintOrder> orders = orderManager.findAll(Pageable.unpaged());
		model.addAttribute("orders", orders);
		return "orderItem";
	}

	/**
	 * @param id ProductIdentifier of the OrderItem that gets removed.
	 * @return redirect to /order.
	 */
	@PostMapping("orders/detail/orderItem/remove/{id}")
	@PreAuthorize("isAuthenticated()")
	public String remove(@PathVariable("id") ProductIdentifier id) {
		orderItemManager.deleteOrderItem(id);
		return "redirect:/orders";
	}
}