package missmint.orders.controllers;

import missmint.Utils;
import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderService;
import missmint.orders.order.PickUpService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.money.MonetaryAmount;
import java.util.Optional;

/**
 * This controller allowed an employee to return a repaired item to a customer.
 */
@Controller
public class PickUpController {
	private final OrderService orderService;
	private PickUpService pickupService;

	public PickUpController(OrderService orderService, PickUpService pickupService) {
		Assert.notNull(orderService, "orderService should not be null");
		Assert.notNull(pickupService, "pickupService should not be null");

		this.orderService = orderService;
		this.pickupService = pickupService;
	}

	/**
	 * Shows the pickup page with the maybe necessary transaction.
	 *
	 * @param optionalOrder The order that should be picked up.
	 * @return The pickup model.
	 */
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

	/**
	 * Performs the actual pickup process after the transaction was accepted.
	 *
	 * @param optionalOrder The order that should be picked up.
	 * @return A redirect to the order overview.
	 */
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@PostMapping("/orders/pickup/{optionalOrder}")
	@PreAuthorize("isAuthenticated()")
	public String pickupForm(@PathVariable Optional<MissMintOrder> optionalOrder) {
		MissMintOrder order = Utils.getOrThrow(optionalOrder);
		if (!order.canPickUp()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		pickupService.pickup(order);

		return "redirect:/orders";
	}
}
