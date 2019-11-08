package missmint.orders.controllers;

import missmint.orders.order.MissMintOrder;
import org.salespointframework.order.OrderManager;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrdersController {

	private final OrderManager<MissMintOrder> orderManager;

	public OrdersController(OrderManager<MissMintOrder> orderManager) {
		this.orderManager = orderManager;
	}

	@RequestMapping("/orders")
	@PreAuthorize("isAuthenticated()")
	public String listOrders(Model model) {
		model.addAttribute("orders", orderManager.findAll(Pageable.unpaged()));
		return "orders";
	}
}
