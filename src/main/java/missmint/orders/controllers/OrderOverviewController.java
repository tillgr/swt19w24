package missmint.orders.controllers;

import missmint.Utils;
import missmint.orders.order.MissMintOrder;
import missmint.orders.service.MissMintService;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Controller for the overview and detail pages of the order.
 */
@Controller
public class OrderOverviewController {
	private final OrderManager<MissMintOrder> orderManager;
	private final Catalog<MissMintService> catalog;

	public OrderOverviewController(OrderManager<MissMintOrder> orderManager, Catalog<MissMintService> catalog) {
		this.orderManager = orderManager;
		this.catalog = catalog;
	}

	/**
	 *	Lists all orders in the system.
	 *
	 * @return The orders template.
	 */
	@GetMapping("/orders")
	@PreAuthorize("isAuthenticated()")
	public String listOrders(Model model) {
		Page<MissMintOrder> orders = orderManager.findAll(Pageable.unpaged());

		model.addAttribute("orders", orders);
		return "orders";
	}

	/**
	 * Returns a detailed overview for an order.
	 *
	 * @param pathOrder The order that should be shown.
	 * @return The orderdetail template.
	 */
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@GetMapping("/orders/detail/{pathOrder}")
	@PreAuthorize("isAuthenticated()")
	public String orderDetail(Model model, @PathVariable Optional<MissMintOrder> pathOrder) {
		MissMintOrder order = Utils.getOrThrow(pathOrder);
		model.addAttribute("order", order);

		ProductIdentifier productIdentifier = Utils.getOrThrow(order.getOrderLines().stream().findAny()).getProductIdentifier();
		MissMintService service = Utils.getOrThrow(catalog.findById(productIdentifier));
		model.addAttribute("service", service);

		return "orderdetail";
	}
}
