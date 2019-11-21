package missmint.orders.controllers;

import missmint.Utils;
import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderService;
import missmint.orders.order.OrderState;
import org.salespointframework.order.OrderManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.money.MonetaryAmount;
import java.util.Optional;

@Controller
public class PickUpController {
	private final OrderService orderService;
	private final OrderManager<MissMintOrder> orderManager;

	public PickUpController(OrderService orderService, OrderManager<MissMintOrder> orderManager) {
		this.orderService = orderService;
		this.orderManager = orderManager;
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@GetMapping("/orders/pickup/{optionalOrder}")
	@PreAuthorize("isAuthenticated()")
	public String pickupPage(Model model, @PathVariable Optional<MissMintOrder> optionalOrder) {
		MissMintOrder order = Utils.getOrThrow(optionalOrder);
		if (!order.canPickUp()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		MonetaryAmount charge = orderService.calculateCharge(order);

		model.addAttribute("charge", charge);
		model.addAttribute("order", order);
		return "pickup";
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@PostMapping("/orders/pickup/{optionalOrder}")
	@PreAuthorize("isAuthenticated()")
	public String pickupForm(@PathVariable Optional<MissMintOrder> optionalOrder) {
		MissMintOrder order = Utils.getOrThrow(optionalOrder);
		if (!order.canPickUp()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		order.setOrderState(OrderState.PICKED_UP);
		orderManager.save(order);

		return "redirect:/orders";
	}
}
