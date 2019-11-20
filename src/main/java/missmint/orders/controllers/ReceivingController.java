package missmint.orders.controllers;

import missmint.orders.forms.ReceivingForm;
import missmint.orders.service.Service;
import org.salespointframework.catalog.Catalog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReceivingController {

	private final Catalog<Service> catalog;

	public ReceivingController(Catalog<Service> catalog) {
		this.catalog = catalog;
	}

	@RequestMapping("/orders/receiving")
	@PreAuthorize("isAuthenticated()")
	public String receiving(Model model, ReceivingForm form) {
		model.addAttribute("form", form);
		model.addAttribute("services", catalog.findByAllCategories());
		return "receiving";
	}
}
