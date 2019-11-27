package missmint.orders.controllers;

import missmint.Utils;
import missmint.inventory.manager.MaterialManager;
import missmint.inventory.products.Material;
import missmint.inventory.products.OrderItem;
import missmint.orders.forms.ReceivingForm;
import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderService;
import missmint.orders.order.ReceivingService;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceConsumptionManager;
import missmint.orders.service.ServiceManager;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
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

@Controller
public class ReceivingController {

	private final BusinessTime time;
	private final OrderManager<MissMintOrder> orderManager;
	private final OrderService orderService;
	private final ReceivingService receivingService;
	private final ServiceManager serviceManager;


	public ReceivingController(ServiceManager serviceManager, BusinessTime businessTime, OrderManager<MissMintOrder> orderManager, OrderService orderService, ReceivingService receivingService) {
		this.serviceManager = serviceManager;
		time = businessTime;
		this.orderManager = orderManager;
		this.orderService = orderService;
		this.receivingService = receivingService;
	}

	@GetMapping("/orders/receiving")
	@PreAuthorize("isAuthenticated()")
	public String receiving(Model model, @ModelAttribute("form") ReceivingForm form) {
		model.addAttribute("services", serviceManager.findAll());
		return "receiving";
	}

	@PostMapping("/orders/receiving")
	@PreAuthorize("isAuthenticated()")
	public String cost(@Valid @ModelAttribute("form") ReceivingForm form, Errors errors, Model model, @LoggedIn UserAccount userAccount, HttpSession session, UniqueInventory<UniqueInventoryItem> materialInventory, Catalog<Material> materialCatalog, MaterialManager materialManager) {
		if (errors.hasErrors()) {
			return receiving(model, form);
		}

		MissMintService service = Utils.getOrThrow(serviceManager.findById(form.getService()));


		if (!orderService.isOrderAcceptable(service)) {
			model.addAttribute("notAcceptable", true);
			return receiving(model, form);
		}

		LocalDate now = time.getTime().toLocalDate();
		MissMintOrder order = new MissMintOrder(userAccount, form.getCustomer(), now, service, new OrderItem(form.getDescription()));
		session.setAttribute("order", order);

		model.addAttribute("total", order.getTotal());
		return "cost";
	}

	@PostMapping("/orders/ticket")
	@PreAuthorize("isAuthenticated()")
	public String ticket(@SessionAttribute("order") MissMintOrder order, Model model) {
		model.addAttribute("order", order);
		receivingService.receiveOrder(order);
		return "ticket";
	}
}
