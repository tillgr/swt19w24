package missmint.inventory.controller;

import missmint.inventory.forms.OrderItemForm;
import missmint.inventory.manager.OrderItemManager;
import missmint.inventory.products.OrderItem;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class OrderItemController {
	private final OrderItemManager orderItemManager;
	private final Catalog<OrderItem> OrderItemCatalog;

	public OrderItemController(OrderItemManager orderItemManager, Catalog<OrderItem> orderItemCatalog) {
		this.orderItemManager = orderItemManager;
		OrderItemCatalog = orderItemCatalog;
	}

	@GetMapping("/orderItem")
	@PreAuthorize("isAuthenticated()")
	public String orderItem(Model model) {
		model.addAttribute("orderItem", OrderItemCatalog.findByCategory("ORDER_ITEM"));
		return "orderItem";
	}

	@PostMapping("/orderItem/remove")
	@PreAuthorize("isAuthenticated()")
	public String removeOrderItem(@Valid @ModelAttribute("form") OrderItemForm orderItemForm){
		ProductIdentifier orderItemId = orderItemForm.getOrderItemId();
		orderItemManager.deleteOrderItem(orderItemId);
		return "redirect:/orderItem";
	}
}
