package missmint.orders.controllers;

import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderState;
import missmint.orders.service.MissMintService;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PickUpControllerTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	OrderManager<MissMintOrder> orderManager;
	@Autowired
	private UserAccountManager userAccountManager;
	@Autowired
	private BusinessTime time;
	@Autowired
	private Catalog<MissMintService> serviceCatalog;

	//@Test
	void unauthenticated() throws Exception {
		mvc.perform(get("/orders/pickup/123"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));

		mvc.perform(post("/orders/pickup/123"))
			.andExpect(status().isForbidden());
	}

	//@Test
	@WithMockUser
	void canNotPickUp() throws Exception {
		MissMintOrder order = createOrder();
		orderManager.save(order);

		mvc.perform(get(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT))
			.andExpect(status().isBadRequest());

		mvc.perform(post(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT).with(csrf()))
			.andExpect(status().isBadRequest());
	}

	//@Test
	@WithMockUser
	void pickupCosts() throws Exception {
		MissMintOrder order = createOrder();
		order.setFinished(time.getTime().toLocalDate().minusDays(15));
		order.setOrderState(OrderState.STORED);
		orderManager.save(order);

		mvc.perform(get(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Please charge the customer EUR 1.")))
			.andExpect(content().string(not(containsString("compensation"))));
	}

	//@Test
	@WithMockUser
	void pickupCompensation() throws Exception {
		MissMintOrder order = createOrder();
		order.setFinished(time.getTime().toLocalDate().minusDays(10));
		order.setOrderState(OrderState.FINISHED);
		orderManager.save(order);

		mvc.perform(get(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Please give the customer a compensation of EUR 5.")))
			.andExpect(content().string(not(containsString("charge"))));
	}

	//@Test
	@WithMockUser
	void pickupZero() throws Exception {
		MissMintOrder order = createOrder();
		order.setFinished(time.getTime().toLocalDate().minusDays(14));
		order.setOrderState(OrderState.STORED);
		orderManager.save(order);

		mvc.perform(get(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT))
			.andExpect(status().isOk())
			.andExpect(content().string(not(containsString("compensation"))))
			.andExpect(content().string(not(containsString("charge"))));
	}

	//@Test
	@WithMockUser
	void pickupPost() throws Exception {
		MissMintOrder order = createOrder();
		order.setFinished(time.getTime().toLocalDate().minusDays(0));
		order.setOrderState(OrderState.FINISHED);
		orderManager.save(order);

		mvc.perform(post(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT).with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/orders"));

		assertThat(order.getId()).isNotNull();
		Optional<MissMintOrder> optionalOrder = orderManager.get(order.getId());
		assertThat(optionalOrder).isNotEmpty();
		assertThat(optionalOrder.get().getOrderState()).isEqualTo(OrderState.PICKED_UP);
	}

	private MissMintOrder createOrder() {
		UserAccount userAccount = userAccountManager.create("alice", Password.UnencryptedPassword.of("password"));
		Optional<MissMintService> optionalService = serviceCatalog.findByName("sewing-buttons").get().findAny();

		assertThat(optionalService).isNotEmpty();

		MissMintService service = optionalService.get();
		LocalDate now = time.getTime().toLocalDate();
		return new MissMintOrder(userAccount, "Bob", now.minusMonths(1), now.minusDays(15), service);
	}
}
