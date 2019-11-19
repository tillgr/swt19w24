package missmint.orders.controllers;

import missmint.orders.order.MissMintOrder;
import missmint.orders.service.Service;
import org.junit.jupiter.api.Test;
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

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderOverviewControllerTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private OrderManager<MissMintOrder> orderManager;
	@Autowired
	private UserAccountManager userAccountManager;
	@Autowired
	private BusinessTime time;
	@Autowired
	private Catalog<Service> serviceCatalog;

	@Test
	void unauthenticated() throws Exception {
		mvc.perform(get("/orders"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));

		mvc.perform(get("/orders/123"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser()
	void noOrders() throws Exception {
		mvc.perform(get("/orders"))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser()
	void orders() throws Exception {
		MissMintOrder order = createOrder();

		mvc.perform(get("/orders"))
			.andExpect(status().isOk())
		.andExpect(content().string(containsString(String.valueOf(order.getId()))))
		.andExpect(content().string(containsString(order.getCustomer())));
		// TODO Customer Item test
		// TODO button tests
	}

	@Test
	@WithMockUser()
	void orderDetailNoOrder() throws Exception {
		mvc.perform(get("/orders/123"))
			.andExpect(status().is5xxServerError());
	}

	@Test
	@WithMockUser()
	void orderDetail() throws Exception {
		MissMintOrder order = createOrder();

		mvc.perform(get(String.format("/orders/%s", order.getId())).locale(Locale.ROOT))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(String.valueOf(order.getId()))))
			.andExpect(content().string(containsString(order.getCustomer())))
			.andExpect(content().string(containsString(order.getInbound().toString())))
			.andExpect(content().string(containsString(order.getExpectedFinished().toString())))
			.andExpect(content().string(containsString(">Expected Finishing Date<")))
			.andExpect(content().string(not(containsString(">Finishing Date<"))))
			.andExpect(content().string(containsString("Waiting")))
			.andExpect(content().string(containsString("Sewing (Buttons)")));
		// TODO Customer Item test
	}

	private MissMintOrder createOrder() {
		UserAccount userAccount = userAccountManager.create("alice", Password.UnencryptedPassword.of("password"));
		Optional<Service> optionalService = serviceCatalog.findByName("sewing-buttons").get().findAny();

		assertThat(optionalService).isNotEmpty();

		Service service = optionalService.get();
		MissMintOrder order = new MissMintOrder(userAccount, "Bob", time.getTime().toLocalDate(), time.getTime().toLocalDate().plusDays(1), service);
		orderManager.save(order);
		return order;
	}
}