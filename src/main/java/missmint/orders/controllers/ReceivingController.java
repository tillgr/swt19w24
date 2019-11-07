package missmint.orders.controllers;

import missmint.orders.forms.ReceivingForm;
import missmint.orders.order.MissMintOrder;
import missmint.orders.services.ServiceRepository;
import org.salespointframework.order.Order;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@SessionAttributes("missMintOrder")
public class ReceivingController {
	private ServiceRepository serviceRepository;
	private BusinessTime time;

	public ReceivingController(ServiceRepository serviceRepository, @Qualifier("defaultBusinessTime") BusinessTime time) {
		this.serviceRepository = serviceRepository;
		this.time = time;
	}

	@GetMapping("/orders/receiving")
	@PreAuthorize("isAuthenticated()")
	public String receiving(Model model, ReceivingForm form) {
		model.addAttribute("form", form);
		model.addAttribute("services", serviceRepository.findAll());
		return "receiving";
	}

	@PostMapping("/orders/receiving")
	@PreAuthorize("isAuthenticated()")
	public String cost(@Valid ReceivingForm form, Errors errors, Model model, @LoggedIn UserAccount userAccount, HttpSession session) {
		if (errors.hasErrors()) {
			return receiving(model, form);
		}

		Order order = new Order(userAccount);
		order.addOrderLine(serviceRepository.findById(form.getService()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)).getProduct(), Quantity.of(1));
		MissMintOrder missMintOrder = new MissMintOrder(form.getCustomer(), order, time.getTime().toLocalDate());
		session.setAttribute("missMintOrder", missMintOrder);

		return "cost";
	}
}
