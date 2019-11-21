package missmint.orders.controllers;

import missmint.orders.order.MissMintOrder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
public class PickUpController {
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@GetMapping("/orders/pickup/{order}")
	@PreAuthorize("isAuthenticated()")
	public String pickupPage(Model model, @PathVariable Optional<MissMintOrder> order) {
		/*
		MissMintOrder missMintOrder = order.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
		if (!missMintOrder.canPickUp()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		MonetaryAmount charge = orderService.calculateCharge(missMintOrder);

		model.addAttribute("charge", charge);
		model.addAttribute("order", missMintOrder);
		return "pickup";
		*/
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}
}
