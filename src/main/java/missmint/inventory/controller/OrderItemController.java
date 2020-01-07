package missmint.inventory.controller;

import missmint.inventory.manager.OrderItemManager;
import missmint.inventory.products.OrderItem;
import missmint.orders.order.MissMintOrder;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderItemController {
	private final OrderItemManager orderItemManager;
	private final Catalog<OrderItem> OrderItemCatalog;
	private final OrderManager<MissMintOrder> orderManager;

	public OrderItemController(OrderItemManager orderItemManager, Catalog<OrderItem> orderItemCatalog, OrderManager<MissMintOrder> orderManager) {
		this.orderItemManager = orderItemManager;
		OrderItemCatalog = orderItemCatalog;
		this.orderManager = orderManager;
	}

	@GetMapping("/orderItem")
	@PreAuthorize("isAuthenticated()")
	public String orderItem(Model model) {
		Page<MissMintOrder> orders = orderManager.findAll(Pageable.unpaged());
		model.addAttribute("orders", orders);
		model.addAttribute("orderItem", OrderItemCatalog.findByCategory("ORDER_ITEM"));
		return "orderItem";
	}

	@PostMapping("orders/detail/orderItem/remove/{id}")
	@PreAuthorize("isAuthenticated()")
	public String remove(@PathVariable("id") ProductIdentifier id){
		orderItemManager.deleteOrderItem(id);
		return "redirect:/orders";
	}
}
