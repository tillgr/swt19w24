package missmint.orders;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrdersController {
	@RequestMapping("/orders")
	//TODO @PreAuthorize("isAuthenticated()")
	public String listOrders() {
		return "orders";
	}
}
