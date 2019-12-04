package missmint.orders.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReceivingControllerTests {
//
//	@Autowired
//	private MockMvc mvc;
//
//	@Autowired
//	private OrderManager<MissMintOrder> orderManager;
//
//	@Autowired
//	private Catalog<MissMintService> serviceCatalog;
//
//	@Autowired
//	private UserAccountManager userAccountManager;
//
//	@Autowired
//	private BusinessTime businessTime;
//
//	//@Test
//	void unauthenticated() throws Exception {
//		mvc.perform(get("/orders/receiving"))
//			.andExpect(status().is3xxRedirection())
//			.andExpect(redirectedUrlPattern("**/login"));
//		mvc.perform(post("/orders/receiving"))
//			.andExpect(status().isForbidden());
//		mvc.perform(post("/orders/ticket"))
//			.andExpect(status().isForbidden());
//	}
//
//	//@Test
//	@WithMockUser
//	void serviceSelect() throws Exception {
//		Optional<MissMintService> optionalService = serviceCatalog.findByName("grindery-knifes").get().findAny();
//		assertThat(optionalService).isNotEmpty();
//		MissMintService service = optionalService.get();
//
//		mvc.perform(get("/orders/receiving").locale(Locale.ROOT))
//			.andExpect(status().isOk())
//			.andExpect(content().string(containsString("Cleaning (Suits)")))
//			.andExpect(content().string(containsString("Grindery (Knifes)")))
//			.andExpect(content().string(containsString(String.valueOf(service.getId()))));
//	}
//
//	//@Test
//	@WithMockUser
//	void receivingFormErrors() throws Exception {
//		mvc.perform(post("/orders/receiving").locale(Locale.ROOT).with(csrf())
//			.param("customer", " ")
//			.param("description", "\t")
//		)
//			.andExpect(status().isOk())
//			.andExpect(view().name("receiving"))
//			.andExpect(content().string(containsString("The customer&#39;s name must not be empty.")))
//			.andExpect(content().string(containsString("The item description must not be empty.")))
//			.andExpect(content().string(containsString("Please select a service.")));
//	}
//
//	//@Test
//	@WithMockUser
//	void receivingForm() throws Exception {
//		Optional<MissMintService> optionalService = serviceCatalog.findByName("grindery-knifes").get().findAny();
//		assertThat(optionalService).isNotEmpty();
//		MissMintService service = optionalService.get();
//
//		mvc.perform(post("/orders/receiving").locale(Locale.ROOT).with(csrf())
//			.param("customer", "Lagrange")
//			.param("description", "Lagrange's theorem")
//			.param("service", String.valueOf(service.getId()))
//		)
//			.andExpect(status().isOk())
//			.andExpect(view().name("cost"))
//			.andExpect(content().string(containsString(String.format("The customer paid %s.", service.getPrice()))))
//			.andExpect(content().string(containsString("orders/ticket")))
//			.andExpect(request().sessionAttribute("order",
//				allOf(isA(MissMintOrder.class),
//					HasPropertyWithValue.hasProperty("customer", equalTo("Lagrange")),
//					HasPropertyWithValue.hasProperty("inbound", notNullValue()),
//					HasPropertyWithValue.hasProperty("expectedFinished", notNullValue()),
//					HasPropertyWithValue.hasProperty("finished", nullValue()),
//					HasPropertyWithValue.hasProperty("orderState", equalTo(OrderState.WAITING))
//				)));
//	}
//
//	//@Test
//	@WithMockUser
//	void ticket() throws Exception {
//		Optional<MissMintService> optionalService = serviceCatalog.findByName("grindery-knifes").get().findAny();
//		assertThat(optionalService).isNotEmpty();
//		MissMintService service = optionalService.get();
//
//		UserAccount userAccount = userAccountManager.create("employee", Password.UnencryptedPassword.of("weakpwd"));
//
//		LocalDate date = businessTime.getTime().toLocalDate();
//		MissMintOrder order = new MissMintOrder(userAccount, "Einstein", date, date.plusDays(1), service);
//
//		mvc.perform(post("/orders/ticket").locale(Locale.ROOT).with(csrf()).sessionAttr("order", order))
//			.andExpect(status().isOk())
//			.andExpect(view().name("ticket"))
//			.andExpect(content().string(containsString(order.getCustomer())))
//			.andExpect(content().string(containsString(String.valueOf(order.getExpectedFinished()))))
//			.andExpect(content().string(containsString("Print")));
//
//		long count = orderManager.findBy(userAccount).get()
//			.peek(o -> {
//				assertThat(o.getCustomer()).isEqualTo(order.getCustomer());
//				assertThat(o.getExpectedFinished()).isEqualTo(order.getExpectedFinished());
//				assertThat(o.getInbound()).isEqualTo(order.getInbound());
//				assertThat(o.getFinished()).isNull();
//				assertThat(o.getOrderState()).isEqualTo(OrderState.WAITING);
//			}).count();
//
//		assertThat(count).isEqualTo(1);
//	}
}
