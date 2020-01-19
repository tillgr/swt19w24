package missmint.time;

import missmint.orders.order.OrderService;
import missmint.users.service.SalaryService;
import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * A Service that handles the forwarding of time.
 */
@Service
public class TimeService {
	private BusinessTime time;
	private OrderService orderService;
	private SalaryService salaryService;

	public TimeService(BusinessTime businessTime, OrderService orderService, SalaryService salaryService) {
		Assert.notNull(businessTime, "businessTime should not be null");
		Assert.notNull(orderService, "orderService should not be null");
		Assert.notNull(salaryService, "salaryService should not be null");

		time = businessTime;
		this.orderService = orderService;
		this.salaryService = salaryService;
	}

	/**
	 * This function is called whenever the time is forwarded to process time dependant events.
	 *
	 * @see BusinessTime#forward(Duration)
	 * @see OrderService#updateOrders()
	 * @see SalaryService#payStaff(Duration)
	 */
	public void forward(Duration duration) {
		Assert.notNull(duration, "duration should not be null");
		Assert.isTrue(!duration.isNegative(), "duration should not be negative");

		time.forward(duration);
		orderService.updateOrders();
		salaryService.payStaff(duration);
	}
}
