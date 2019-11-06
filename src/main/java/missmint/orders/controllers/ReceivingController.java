package missmint.orders.controllers;

import missmint.orders.services.ServiceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReceivingController {
	private ServiceRepository serviceRepository;

	public ReceivingController(ServiceRepository serviceRepository) {
		this.serviceRepository = serviceRepository;
	}

	@RequestMapping("/receiving")
	//TODO @PreAuthorize("isAuthenticated()")
	public String listOrders(Model model) {
		return "receiving";
	}
}
