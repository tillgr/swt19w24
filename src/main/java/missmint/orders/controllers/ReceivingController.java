package missmint.orders.controllers;

import missmint.orders.forms.ReceivingForm;
import missmint.orders.order.MissMintOrder;
import missmint.orders.services.ServiceRepository;
import org.salespointframework.order.Order;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
public class ReceivingController {
	private ServiceRepository serviceRepository;
	private BusinessTime time;

	public ReceivingController(ServiceRepository serviceRepository, BusinessTime time) {
		this.serviceRepository = serviceRepository;
		this.time = time;
	}

	@GetMapping("/orders/receiving")
	//TODO @PreAuthorize("isAuthenticated()")
	public String receiving(Model model, ReceivingForm form) {
		model.addAttribute("form", form);
		model.addAttribute("services", serviceRepository.findAll());
		return "receiving";
	}

	@PostMapping("/orders/receiving")
	//TODO @PreAuthorize("isAuthenticated()")
	public String cost(@Valid ReceivingForm receivingForm, Errors errors, Model model , @LoggedIn UserAccount userAccount) {
		if (errors.hasErrors()) {
			return receiving(model, receivingForm);
		}

		Order order = new Order(userAccount);
		order.addOrderLine(serviceRepository.findById(receivingForm.getService()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)).getProduct(), Quantity.of(1));
		MissMintOrder missMintOrder = new MissMintOrder(receivingForm.getCustomer(), order, time.getTime().toLocalDate());

		return "cost";
	}
}
