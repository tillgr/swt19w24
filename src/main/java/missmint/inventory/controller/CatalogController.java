package missmint.inventory.controller;

import missmint.inventory.products.OrderItem;
import org.salespointframework.catalog.Catalog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CatalogController {

	private final Catalog<OrderItem> OrderItemCatalog;

	CatalogController(Catalog<OrderItem> OrderItemCatalog) {
		this.OrderItemCatalog = OrderItemCatalog;
	}

	@GetMapping("/orderItem")
	@PreAuthorize("isAuthenticated()")
	public String orderItem(Model model) {
		model.addAttribute("orderItem", OrderItemCatalog.findByCategory("ORDER_ITEM"));
		return "orderItem";
	}
}
