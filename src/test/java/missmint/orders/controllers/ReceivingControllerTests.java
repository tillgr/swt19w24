package missmint.orders.controllers;

import missmint.inventory.products.Material;
import missmint.inventory.products.OrderItem;
import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderState;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceCategory;
import missmint.rooms.RoomRepository;
import missmint.time.EntryRepository;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.OrderManager;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReceivingControllerTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private OrderManager<MissMintOrder> orderManager;

	@Autowired
	private Catalog<MissMintService> serviceCatalog;

	@Autowired
	private UserAccountManager userAccountManager;

	@Autowired
	private BusinessTime businessTime;

	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private UniqueInventory<UniqueInventoryItem> inventory;

	@Autowired
	private Catalog<Material> materialCatalog;

	@Autowired
	private EntryRepository entryRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Test
	void unauthenticated() throws Exception {
		mvc.perform(get("/orders/receiving"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));
		mvc.perform(post("/orders/receiving"))
			.andExpect(status().isForbidden());
		mvc.perform(post("/orders/ticket"))
			.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser
	void serviceSelect() throws Exception {
		MissMintService service = getService();

		mvc.perform(get("/orders/receiving").locale(Locale.ROOT))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Cleaning (Suits)")))
			.andExpect(content().string(containsString("Grindery (Knifes)")))
			.andExpect(content().string(containsString(String.valueOf(service.getId()))));
	}

	@Test
	@WithMockUser
	void receivingFormErrors() throws Exception {
		mvc.perform(post("/orders/receiving").locale(Locale.ROOT).with(csrf())
			.param("customer", " ")
			.param("description", "\t")
		)
			.andExpect(status().isOk())
			.andExpect(view().name("receiving"))
			.andExpect(content().string(containsString("The customer&#39;s name must not be empty.")))
			.andExpect(content().string(containsString("The item description must not be empty.")))
			.andExpect(content().string(containsString("Please select a service.")));
	}

	@Test
	@WithMockUser
	void noSkilledStaff() throws Exception {
		MissMintService service = getService();
		staffRepository.findAll().forEach(s -> {
			s.getSkills().clear();
			staffRepository.save(s);
		});

		mvc.perform(post("/orders/receiving").locale(Locale.ROOT).with(csrf())
				.param("customer", "Turing")
				.param("description", "Maschine")
				.param("service", String.valueOf(service.getId()))
		)
				.andExpect(status().isOk())
				.andExpect(view().name("receiving"))
				.andExpect(content().string(containsString("This service is currently unavailable.")));
	}

	@Test
	@WithMockUser
	void noRoom() throws Exception {
		MissMintService service = getService();
		createStaff();
		roomRepository.deleteAll();

		mvc.perform(post("/orders/receiving").locale(Locale.ROOT).with(csrf())
				.param("customer", "Elon Musk")
				.param("description", "Starlink")
				.param("service", String.valueOf(service.getId()))
		)
				.andExpect(status().isOk())
				.andExpect(view().name("receiving"))
				.andExpect(content().string(containsString("This service is currently unavailable.")));
	}

	@Test
	@WithMockUser
	void receivingForm() throws Exception {
		MissMintService service = getService();

		createStaff();

		mvc.perform(post("/orders/receiving").locale(Locale.ROOT).with(csrf())
			.param("customer", "Lagrange")
			.param("description", "Lagrange's theorem")
			.param("service", String.valueOf(service.getId()))
		)
			.andExpect(status().isOk())
			.andExpect(view().name("cost"))
			.andExpect(content().string(containsString(String.format("The customer paid %s.", service.getPrice()))))
			.andExpect(content().string(containsString("orders/ticket")))
			.andExpect(request().sessionAttribute("order",
				allOf(isA(MissMintOrder.class),
					HasPropertyWithValue.hasProperty("customer", equalTo("Lagrange")),
					HasPropertyWithValue.hasProperty("inbound", notNullValue()),
					HasPropertyWithValue.hasProperty("expectedFinished", nullValue()),
					HasPropertyWithValue.hasProperty("finished", nullValue()),
					HasPropertyWithValue.hasProperty("orderState", equalTo(OrderState.WAITING)),
					HasPropertyWithValue.hasProperty("item",
						HasPropertyWithValue.hasProperty("name", equalTo("Lagrange's theorem"))
					)
				)));
	}

	@Test
	@WithMockUser
	void ticket() throws Exception {
		MissMintService service = getService();

		UserAccount userAccount = createStaff().getUserAccount();

		LocalDate date = businessTime.getTime().toLocalDate();
		MissMintOrder order = new MissMintOrder(userAccount, "Einstein", date, service, new OrderItem("relativity theory"));

		Optional<Material> optionalMaterial = materialCatalog.findByName("sanding paper").get().findAny();
		assertThat(optionalMaterial).isNotEmpty();
		Optional<UniqueInventoryItem> optionalInventoryItem = inventory.findByProduct(optionalMaterial.get());
		assertThat(optionalInventoryItem).isNotEmpty();
		UniqueInventoryItem inventoryItem = optionalInventoryItem.get();
		inventoryItem.decreaseQuantity(inventoryItem.getQuantity().subtract(Quantity.of(20, inventoryItem.getQuantity().getMetric())));
		inventory.save(inventoryItem);

		mvc.perform(post("/orders/ticket").locale(Locale.ROOT).with(csrf()).sessionAttr("order", order))
			.andExpect(status().isOk())
			.andExpect(view().name("ticket"))
			.andExpect(content().string(containsString(order.getCustomer())))
			.andExpect(content().string(containsString(String.valueOf(order.getExpectedFinished()))))
			.andExpect(content().string(containsString("Print")));

		long count = orderManager.findBy(userAccount).get()
			.peek(o -> {
				assertThat(o.getCustomer()).isEqualTo(order.getCustomer());
				assertThat(o.getExpectedFinished()).isNotNull();
				assertThat(o.getExpectedFinished()).isAfterOrEqualTo(date);
				assertThat(o.getInbound()).isEqualTo(order.getInbound());
				assertThat(o.getFinished()).isNull();
				assertThat(o.getOrderState()).isEqualTo(OrderState.WAITING);
				assertThat(o.getItem()).isNotNull();
				assertThat(o.getItem().getName()).isEqualTo("relativity theory");

				long entries = Streamable.of(entryRepository.findAll()).get().peek(e -> {
					assertThat(e.getOrder()).isEqualTo(o);
					assertThat(e.getBeginning()).isAfter(businessTime.getTime());
				}).count();
				assertThat(entries).isEqualTo(1);
			}).count();
		assertThat(count).isEqualTo(1);

		optionalInventoryItem = inventory.findByProduct(optionalMaterial.get());
		assertThat(optionalInventoryItem).isNotEmpty();
		assertThat(optionalInventoryItem.get().getQuantity().getAmount()).isEqualByComparingTo(BigDecimal.valueOf(2, -1));
	}

	private MissMintService getService() {
		Optional<MissMintService> optionalService = serviceCatalog.findByName("grindery-knifes").get().findAny();
		assertThat(optionalService).isNotEmpty();
		return optionalService.get();
	}

	private Staff createStaff() {
		UserAccount userAccount = userAccountManager.create("employee", Password.UnencryptedPassword.of("weakpwd"));
		Staff staff = new Staff(userAccount, "Mad", "Max");
		staff.addSkill(ServiceCategory.GRINDERY);
		return staffRepository.save(staff);
	}
}
