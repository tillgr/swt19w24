package missmint.time;

import missmint.orders.order.OrderService;
import missmint.users.service.SalaryService;
import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TimeService {
	private BusinessTime time;
	private OrderService orderService;
	private SalaryService salaryService;

	public TimeService(BusinessTime businessTime, OrderService orderService, SalaryService salaryService) {
		time = businessTime;
		this.orderService = orderService;
		this.salaryService = salaryService;
	}

	/**
	 * This function is called whenever the time is forwarded to process time dependant events.
	 */
	public void forward(Duration duration) {
		time.forward(duration);
		orderService.updateOrders();
		salaryService.payStaff(duration);
	}
}
