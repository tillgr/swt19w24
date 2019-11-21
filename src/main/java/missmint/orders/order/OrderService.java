package missmint.orders.order;

import missmint.orders.order.MissMintOrder;
import org.javamoney.moneta.Money;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Service
public class OrderService {
	private final OrderManager<MissMintOrder> orderManager;
	private final BusinessTime businessTime;

	@Value("${general.currency}")
	private String currency;
	@Value("${orders.pickup.compensation.percent}")
	private long compensation;

	public OrderService(OrderManager<MissMintOrder> orderManager, BusinessTime businessTime) {
		this.orderManager = orderManager;
		this.businessTime = businessTime;
	}

	public MonetaryAmount calculateCharge(MissMintOrder order) {
		Assert.notNull(order, "order must not be null");
		Assert.isTrue(order.canPickUp(), "order can not be picked up");

		long daysTooLate = ChronoUnit.DAYS.between(order.getExpectedFinished(), order.getFinished());
		var deduction = BigDecimal.valueOf(compensation, 2).multiply(BigDecimal.valueOf(Math.min(Math.max(daysTooLate, 0), 10)));

		MonetaryAmount charge = order.getTotal().multiply(deduction).negate();

		if (order.getOrderState() == OrderState.FINISHED) {
			return charge;
		}

		long weeksStored = ChronoUnit.WEEKS.between(order.getLatestFinished(), businessTime.getTime().toLocalDate());

		Assert.isTrue(weeksStored > 0, "order has to be in the shop for a week or more to be stored");
		return charge.add(Money.of(BigDecimal.valueOf(5, 1), currency).multiply(weeksStored));
	}
}
