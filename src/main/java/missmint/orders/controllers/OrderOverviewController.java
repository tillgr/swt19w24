package missmint.orders.controllers;

import missmint.Utils;
import missmint.orders.order.MissMintOrder;
import missmint.orders.service.Service;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

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

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@RequestMapping("/orders/{pathOrder}")
	@PreAuthorize("isAuthenticated()")
	public String orderDetail(Model model, @PathVariable Optional<MissMintOrder> pathOrder) {
		MissMintOrder order = Utils.getOrThrow(pathOrder);
		model.addAttribute("order", order);

		ProductIdentifier productIdentifier = Utils.getOrThrow(order.getOrderLines().stream().findAny()).getProductIdentifier();
		Service service = Utils.getOrThrow(catalog.findById(productIdentifier));
		model.addAttribute("service", service);

		return "orderdetail";
	}
}
