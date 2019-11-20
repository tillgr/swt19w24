package missmint.inventory.controller;

import missmint.inventory.products.orderItem;
import org.salespointframework.catalog.Catalog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CatalogController {

	private final Catalog<Material> materialCatalog;
	private final Catalog<orderItem> orderItemCatalog;

	CatalogController(Catalog<Material> materialCatalog, Catalog<orderItem> orderItemCatalog){
		this.orderItemCatalog = orderItemCatalog;
		this.materialCatalog = materialCatalog;
	}



	@GetMapping("/orderItem")
	String orderItem(Model model) {
		model.addAttribute("orderItem", orderItemCatalog.findByCategory("ORDER_ITEM"));
		return "orderItem";
	}
}
