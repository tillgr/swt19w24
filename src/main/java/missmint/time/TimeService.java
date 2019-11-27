package missmint.time;

import missmint.orders.order.OrderService;
import org.springframework.stereotype.Service;

@Service
public class TimeService {
	private OrderService orderService;

	public TimeService(OrderService orderService) {
		this.orderService = orderService;
	}

	/**
	 * This function is called whenever the time is forwarded to process time dependant events.
	 */
	public void onForward() {
		orderService.updateOrders();
		// TODO pay staff
	}
}
