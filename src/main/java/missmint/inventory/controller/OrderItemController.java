package missmint.inventory.controller;

import missmint.inventory.manager.OrderItemManager;
import missmint.inventory.products.OrderItem;
import missmint.orders.order.MissMintOrder;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.order.OrderManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderItemController {
	private final OrderManager<MissMintOrder> orderManager;

	public OrderItemController(OrderManager<MissMintOrder> orderManager) {
		this.orderManager = orderManager;
	}

	/**
	 *	Lists all Order-Items in the system.
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
}
