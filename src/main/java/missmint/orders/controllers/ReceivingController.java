package missmint.orders.controllers;

import missmint.orders.forms.ReceivingForm;
import missmint.orders.service.Service;
import org.salespointframework.catalog.Catalog;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
public class ReceivingController {

	private final Catalog<Service> catalog;

	public ReceivingController(Catalog<Service> catalog) {
		this.catalog = catalog;
	}

	@RequestMapping("/orders/receiving")
	@PreAuthorize("isAuthenticated()")
	public String receiving(Model model, @ModelAttribute("form") ReceivingForm form) {
		model.addAttribute("services", catalog.findByAllCategories());
		return "receiving";
	}

	@PostMapping("/orders/receiving")
	@PreAuthorize("isAuthenticated()")
	public String cost(@Valid @ModelAttribute("form") ReceivingForm form, Errors errors, Model model/*, @LoggedIn UserAccount userAccount, HttpSession session*/) {
		if (errors.hasErrors()) {
			return receiving(model, form);
		}

		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
	}
}
