package missmint.orders;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReceivingController {
	@RequestMapping("/receiving")
	//TODO @PreAuthorize("isAuthenticated()")
	public String listOrders() {
		return "receiving";
	}
}
