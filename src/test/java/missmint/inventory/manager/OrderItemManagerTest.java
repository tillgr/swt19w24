package missmint.inventory.manager;

import missmint.inventory.products.OrderItem;
import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderState;
import missmint.orders.service.MissMintService;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Transactional
public class OrderItemManagerTest {


	@Autowired
	private UserAccountManager userAccountManager;

	@Autowired
	private Catalog<MissMintService> serviceCatalog;

	@Autowired
	private Catalog<OrderItem> itemCatalog;

	@Autowired
	private BusinessTime time;

	@Autowired
	private OrderManager<MissMintOrder> orderManager;

	@Autowired
	private  OrderItemManager orderItemManager;

	private MissMintOrder createOrder() {
		UserAccount userAccount = userAccountManager.create("alice", Password.UnencryptedPassword.of("password"));

		OrderItem orderItem = itemCatalog.save(new OrderItem("encrypted message"));

		Optional<MissMintService> optionalService = serviceCatalog.findByName("sewing-buttons").get().findAny();
		assertThat(optionalService).isNotEmpty();
		MissMintService service = optionalService.get();

		LocalDate now = time.getTime().toLocalDate();
		MissMintOrder order = new MissMintOrder(userAccount, "Bob", now, service, orderItem);
		order.setExpectedFinished(now.plusDays(1));
		orderManager.save(order);
		return order;
	}

	@Test
	void cancelOrder(){
		MissMintOrder order = createOrder();
		LocalDate now = time.getTime().toLocalDate();
		order.setExpectedFinished(now);
		order.setFinished(now.plusDays(7));
		order.setInbound(now.minusYears(1));
		order.setOrderState(OrderState.IN_PROGRESS);

		orderItemManager.deleteOrderItem(order.getItem().getId());
		assertThat(order.getOrderState()).isEqualByComparingTo(OrderState.PICKED_UP);
		assertThat(order.getItem()).isEqualTo(null);
	}
}
