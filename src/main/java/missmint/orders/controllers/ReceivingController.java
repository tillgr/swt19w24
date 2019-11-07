package missmint.orders.controllers;

import missmint.orders.forms.ReceivingForm;
import missmint.orders.order.MissMintOrder;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.OrderManager;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@SessionAttributes("missMintOrder")
public class ReceivingController {
	private Catalog<Product> catalog;
	private BusinessTime time;
	private OrderManager<MissMintOrder> orderManager;

	public ReceivingController(Catalog<Product> catalog, @Qualifier("defaultBusinessTime") BusinessTime time, OrderManager<MissMintOrder> orderManager) {
		this.catalog = catalog;
		this.time = time;
		this.orderManager = orderManager;
	}

	@GetMapping("/orders/receiving")
	@PreAuthorize("isAuthenticated()")
	public String receiving(Model model, ReceivingForm form) {
		model.addAttribute("form", form);
		model.addAttribute("services", catalog.findAll());
		return "receiving";
	}

	@PostMapping("/orders/receiving")
	@PreAuthorize("isAuthenticated()")
	public String cost(@Valid ReceivingForm form, Errors errors, Model model, @LoggedIn UserAccount userAccount, HttpSession session) {
		if (errors.hasErrors()) {
			return receiving(model, form);
		}

		Product service = catalog.findById(form.getService()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
		MissMintOrder missMintOrder = new MissMintOrder(userAccount, form.getCustomer(), time.getTime().toLocalDate(), service);
		session.setAttribute("missMintOrder", missMintOrder);

		model.addAttribute("total", missMintOrder.getTotal());
		return "cost";
	}

	@PostMapping("/orders/ticket")
	@PreAuthorize("isAuthenticated()")
	public String ticket(@ModelAttribute MissMintOrder missMintOrder, Model model) {
		orderManager.save(missMintOrder);
		return "ticket";
	}
}
