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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
	private Catalog<Service> serviceCatalog;

	@Test
	void unauthenticated() throws Exception {
		mvc.perform(get("/orders/pickup/123"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));

		mvc.perform(post("/orders/pickup/123"))
			.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser
	void canNotPickUp() throws Exception {
		MissMintOrder order = createOrder();
		orderManager.save(order);

		mvc.perform(get(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT))
			.andExpect(status().isBadRequest());

		mvc.perform(post(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT).with(csrf()))
			.andExpect(status().isBadRequest());
	}

	private MissMintOrder createOrder() {
		UserAccount userAccount = userAccountManager.create("alice", Password.UnencryptedPassword.of("password"));
		Optional<Service> optionalService = serviceCatalog.findByName("sewing-buttons").get().findAny();

		assertThat(optionalService).isNotEmpty();

		Service service = optionalService.get();
		LocalDate now = time.getTime().toLocalDate();
		return new MissMintOrder(userAccount, "Bob", now.minusMonths(1), now.minusDays(8), service);
	}
}
