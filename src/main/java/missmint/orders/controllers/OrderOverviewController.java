package missmint.orders.controllers;

import missmint.orders.order.MissMintOrder;
import missmint.orders.service.Service;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.order.OrderManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderOverviewController {
	private final OrderManager<MissMintOrder> orderManager;
	private final Catalog<Service> catalog;

	public OrderOverviewController(OrderManager<MissMintOrder> orderManager, Catalog<Service> catalog) {
		this.orderManager = orderManager;
		this.catalog = catalog;
	}

	@RequestMapping("/orders")
	@PreAuthorize("isAuthenticated()")
	public String listOrders(Model model) {
		Page<MissMintOrder> orders = orderManager.findAll(Pageable.unpaged());

		model.addAttribute("orders", orders);
		return "orders";
	}
}
