package missmint.orders.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PickUpControllerTests {

//	@Autowired
//	private MockMvc mvc;
//
//	@Autowired
//	OrderManager<MissMintOrder> orderManager;
//	@Autowired
//	private UserAccountManager userAccountManager;
//	@Autowired
//	private BusinessTime time;
//	@Autowired
//	private Catalog<MissMintService> serviceCatalog;
//
//	//@Test
//	void unauthenticated() throws Exception {
//		mvc.perform(get("/orders/pickup/123"))
//			.andExpect(status().is3xxRedirection())
//			.andExpect(redirectedUrlPattern("**/login"));
//
//		mvc.perform(post("/orders/pickup/123"))
//			.andExpect(status().isForbidden());
//	}
//
//	//@Test
//	@WithMockUser
//	void canNotPickUp() throws Exception {
//		MissMintOrder order = createOrder();
//		orderManager.save(order);
//
//		mvc.perform(get(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT))
//			.andExpect(status().isBadRequest());
//
//		mvc.perform(post(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT).with(csrf()))
//			.andExpect(status().isBadRequest());
//	}
//
//	//@Test
//	@WithMockUser
//	void pickupCosts() throws Exception {
//		MissMintOrder order = createOrder();
//		order.setFinished(time.getTime().toLocalDate().minusDays(15));
//		order.setOrderState(OrderState.STORED);
//		orderManager.save(order);
//
//		mvc.perform(get(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT))
//			.andExpect(status().isOk())
//			.andExpect(content().string(containsString("Please charge the customer EUR 1.")))
//			.andExpect(content().string(not(containsString("compensation"))));
//	}
//
//	//@Test
//	@WithMockUser
//	void pickupCompensation() throws Exception {
//		MissMintOrder order = createOrder();
//		order.setFinished(time.getTime().toLocalDate().minusDays(10));
//		order.setOrderState(OrderState.FINISHED);
//		orderManager.save(order);
//
//		mvc.perform(get(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT))
//			.andExpect(status().isOk())
//			.andExpect(content().string(containsString("Please give the customer a compensation of EUR 5.")))
//			.andExpect(content().string(not(containsString("charge"))));
//	}
//
//	//@Test
//	@WithMockUser
//	void pickupZero() throws Exception {
//		MissMintOrder order = createOrder();
//		order.setFinished(time.getTime().toLocalDate().minusDays(14));
//		order.setOrderState(OrderState.STORED);
//		orderManager.save(order);
//
//		mvc.perform(get(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT))
//			.andExpect(status().isOk())
//			.andExpect(content().string(not(containsString("compensation"))))
//			.andExpect(content().string(not(containsString("charge"))));
//	}
//
//	//@Test
//	@WithMockUser
//	void pickupPost() throws Exception {
//		MissMintOrder order = createOrder();
//		order.setFinished(time.getTime().toLocalDate().minusDays(0));
//		order.setOrderState(OrderState.FINISHED);
//		orderManager.save(order);
//
//		mvc.perform(post(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT).with(csrf()))
//			.andExpect(status().is3xxRedirection())
//			.andExpect(redirectedUrl("/orders"));
//
//		assertThat(order.getId()).isNotNull();
//		Optional<MissMintOrder> optionalOrder = orderManager.get(order.getId());
//		assertThat(optionalOrder).isNotEmpty();
//		assertThat(optionalOrder.get().getOrderState()).isEqualTo(OrderState.PICKED_UP);
//	}
//
//	private MissMintOrder createOrder() {
//		UserAccount userAccount = userAccountManager.create("alice", Password.UnencryptedPassword.of("password"));
//		Optional<MissMintService> optionalService = serviceCatalog.findByName("sewing-buttons").get().findAny();
//
//		assertThat(optionalService).isNotEmpty();
//
//		MissMintService service = optionalService.get();
//		LocalDate now = time.getTime().toLocalDate();
//		return new MissMintOrder(userAccount, "Bob", now.minusMonths(1), now.minusDays(15), service);
//	}
}
