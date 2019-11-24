package missmint.orders.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderOverviewControllerTests {

//	@Autowired
//	private MockMvc mvc;
//
//	@Autowired
//	private OrderManager<MissMintOrder> orderManager;
//	@Autowired
//	private UserAccountManager userAccountManager;
//	@Autowired
//	private BusinessTime time;
//	@Autowired
//	private Catalog<MissMintService> serviceCatalog;
//
//	//@Test
//	void unauthenticated() throws Exception {
//		mvc.perform(get("/orders"))
//			.andExpect(status().is3xxRedirection())
//			.andExpect(redirectedUrlPattern("**/login"));
//
//		mvc.perform(get("/orders/detail/123"))
//			.andExpect(status().is3xxRedirection())
//			.andExpect(redirectedUrlPattern("**/login"));
//	}
//
//	//@Test
//	@WithMockUser()
//	void noOrders() throws Exception {
//		mvc.perform(get("/orders"))
//			.andExpect(status().isOk());
//	}
//
//	//@Test
//	@WithMockUser()
//	void orders() throws Exception {
//		MissMintOrder order = createOrder();
//
//		mvc.perform(get("/orders"))
//			.andExpect(status().isOk())
//			.andExpect(content().string(containsString(String.valueOf(order.getId()))))
//			.andExpect(content().string(containsString(order.getCustomer())))
//			.andExpect(content().string(containsString(String.format("/orders/detail/%s", order.getId()))))
//			.andExpect(content().string(not(containsString(String.format("/orders/pickup/%s", order.getId())))));
//
//		// TODO Customer Item test
//	}
//
//
//	//@Test
//	@WithMockUser()
//	void ordersReturn() throws Exception {
//		UserAccount userAccount = userAccountManager.create("alice", Password.UnencryptedPassword.of("password"));
//		Optional<MissMintService> optionalService = serviceCatalog.findByName("sewing-buttons").get().findAny();
//
//		assertThat(optionalService).isNotEmpty();
//
//		MissMintService service = optionalService.get();
//		MissMintOrder order = new MissMintOrder(userAccount, "Bob", time.getTime().toLocalDate().minusWeeks(1), time.getTime().toLocalDate().plusDays(1), service);
//		order.setOrderState(OrderState.FINISHED);
//		orderManager.save(order);
//
//		mvc.perform(get("/orders/").locale(Locale.ROOT))
//			.andExpect(status().isOk())
//			.andExpect(content().string(containsString(String.valueOf(order.getId()))))
//			.andExpect(content().string(containsString(String.format("/orders/pickup/%s", order.getId()))));
//	}
//
//	//@Test
//	@WithMockUser()
//	void orderDetailNoOrder() throws Exception {
//		mvc.perform(get("/orders/detail/123"))
//			.andExpect(status().is5xxServerError());
//	}
//
//	//@Test
//	@WithMockUser()
//	void orderDetail() throws Exception {
//		MissMintOrder order = createOrder();
//
//		mvc.perform(get(String.format("/orders/detail/%s", order.getId())).locale(Locale.ROOT))
//			.andExpect(status().isOk())
//			.andExpect(content().string(containsString(String.valueOf(order.getId()))))
//			.andExpect(content().string(containsString(order.getCustomer())))
//			.andExpect(content().string(containsString(order.getInbound().toString())))
//			.andExpect(content().string(containsString(order.getExpectedFinished().toString())))
//			.andExpect(content().string(containsString(">Expected Finishing Date<")))
//			.andExpect(content().string(not(containsString(">Finishing Date<"))))
//			.andExpect(content().string(containsString("Waiting")))
//			.andExpect(content().string(containsString("Sewing (Buttons)")));
//		// TODO Customer Item test
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
//		MissMintOrder order = new MissMintOrder(userAccount, "Bob", now, now.plusDays(1), service);
//		orderManager.save(order);
//		return order;
//	}
}