package missmint.orders.order;

import missmint.inventory.products.OrderItem;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceCategory;
import missmint.rooms.Room;
import missmint.rooms.RoomRepository;
import missmint.time.EntryRepository;
import missmint.time.TimeTableEntry;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Transactional
public class OrderServiceTests {

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
	private OrderService orderService;

	@Autowired
	private EntryRepository entryRepository;

	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Value("${general.currency}")
	private String currency;

	@Test
	void chargeCanNotPickUp() {
		MissMintOrder order = createOrder();
		assertThatThrownBy(() -> orderService.calculateCharge(order)).isNotNull();
	}

	@Test
	void chargeNoTransaction() {
		MissMintOrder order = createOrder();
		LocalDate now = time.getTime().toLocalDate();
		order.setExpectedFinished(now.minusDays(1));
		order.setFinished(now.minusDays(1));
		order.setInbound(now.minusYears(1));
		order.setOrderState(OrderState.FINISHED);

		assertThat(orderService.calculateCharge(order)).isEqualByComparingTo(Money.of(0, currency));

		order.setExpectedFinished(now.minusDays(15));
		order.setFinished(now.minusDays(14));
		order.setOrderState(OrderState.STORED);
		assertThat(orderService.calculateCharge(order)).isEqualByComparingTo(Money.of(0, currency));
	}

	@Test
	void chargeCompensation() {
		MissMintOrder order = createOrder();
		LocalDate now = time.getTime().toLocalDate();
		order.setExpectedFinished(now.minusDays(2));
		order.setFinished(now.minusDays(1));
		order.setInbound(now.minusYears(1));
		order.setOrderState(OrderState.FINISHED);

		assertThat(orderService.calculateCharge(order)).isEqualByComparingTo(Money.of(-1, currency));
	}

	@Test
	void chargeFee() {
		MissMintOrder order = createOrder();
		LocalDate now = time.getTime().toLocalDate();
		order.setExpectedFinished(now.minusDays(7));
		order.setFinished(now.minusDays(7));
		order.setInbound(now.minusYears(1));
		order.setOrderState(OrderState.STORED);

		assertThat(orderService.calculateCharge(order)).isEqualByComparingTo(Money.of(0.5, currency));
	}

	@Test
	void updateFullProgression() {
		MissMintOrder order = createOrder();
		LocalDate tomorrow = time.getTime().toLocalDate().plusDays(1);
		TimeTableEntry entry = entryRepository.save(new TimeTableEntry(order, createStaff(), 0, createRoom(), tomorrow));
		order.setEntry(entry);
		orderManager.save(order);

		time.forward(Duration.ofDays(1000));
		orderService.updateOrders();

		Optional<MissMintOrder> optionalOrder = orderManager.findAll(Pageable.unpaged()).get().findAny();
		assertThat(optionalOrder).isPresent();
		order = optionalOrder.get();
		assertThat(order.getEntry()).isEqualTo(entry);
		assertThat(order.getOrderState()).isEqualTo(OrderState.CHARITABLE_USED);
		assertThat(order.getItem()).isNull();
		assertThat(order.getFinished()).isEqualTo(tomorrow);
	}

	private Room createRoom() {
		Room room = new Room("Colosseum in Rome");
		return roomRepository.save(room);
	}

	private Staff createStaff() {
		UserAccount userAccount = userAccountManager.create("employee", Password.UnencryptedPassword.of("weakpwd"));
		Staff staff = new Staff(userAccount, "Mad", "Max", BigDecimal.valueOf(0));
		staff.addSkill(ServiceCategory.GRINDERY);
		return staffRepository.save(staff);
	}

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
}
