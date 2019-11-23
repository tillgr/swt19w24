package missmint.orders.controllers;

import missmint.Utils;
import missmint.orders.forms.ReceivingForm;
import missmint.orders.order.MissMintOrder;
import missmint.orders.service.Service;
import missmint.orders.service.ServiceManager;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Set;

@Controller
public class ReceivingController {

	private final BusinessTime time;
	private final OrderManager<MissMintOrder> orderManager;
	private final ServiceManager serviceManager;

	public ReceivingController(ServiceManager serviceManager, BusinessTime businessTime, OrderManager<MissMintOrder> orderManager) {
		this.serviceManager = serviceManager;
		time = businessTime;
		this.orderManager = orderManager;
	}

	@GetMapping("/orders/receiving")
	@PreAuthorize("isAuthenticated()")
	public String receiving(Model model, @ModelAttribute("form") ReceivingForm form) {
		model.addAttribute("services", serviceManager.findAll());
		return "receiving";
	}

	@PostMapping("/orders/receiving")
	@PreAuthorize("isAuthenticated()")
	public String cost(@Valid @ModelAttribute("form") ReceivingForm form, Errors errors, Model model, @LoggedIn UserAccount userAccount, HttpSession session) {
		if (errors.hasErrors()) {
			return receiving(model, form);
		}

		Service service = Utils.getOrThrow(serviceManager.findById(form.getService()));

		LocalDate now = time.getTime().toLocalDate();
		MissMintOrder order = new MissMintOrder(userAccount, form.getCustomer(), now, now.plusDays(1), service);
		session.setAttribute("order", order);

		model.addAttribute("total", order.getTotal());
		return "cost";
	}

	@PostMapping("/orders/ticket")
	@PreAuthorize("isAuthenticated()")
	public String ticket(@SessionAttribute("order") MissMintOrder order, Model model) {
		model.addAttribute("order", order);
		orderManager.save(order);
		return "ticket";

		// TODO time table planning, creating customer item, finance
	}
}
