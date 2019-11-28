package missmint.inventory.controller;

import missmint.inventory.products.OrderItem;
import org.salespointframework.catalog.Catalog;
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
	public String orderItem(Model model) {
		model.addAttribute("orderItem", OrderItemCatalog.findByCategory("ORDER_ITEM"));
		return "orderItem";
	}
}
