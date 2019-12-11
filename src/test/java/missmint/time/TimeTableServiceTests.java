package missmint.time;

import missmint.Utils;
import missmint.inventory.products.OrderItem;
import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderState;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceManager;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
import missmint.users.service.StaffManagement;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Transactional
public class TimeTableServiceTests {

	@Autowired
	private Catalog<MissMintService> serviceCatalog;
	@Autowired
	private OrderManager<MissMintOrder> orderManager;
	@Autowired
	private StaffRepository staffRepository;
	@Autowired
	private Catalog<OrderItem> itemCatalog;
	@Autowired
	private UserAccountManager userAccountManager;
	@Autowired
	private TimeTableService timeTableService;
	@Autowired
	private EntryRepository entryRepository;
	@Autowired
	private BusinessTime time;
	@Autowired
	private EntityManager entityManager;

	@Test
	void rebuildTimeTable() {
		OrderCreationDTO oldOrder = createOrder("sewing-buttons", false, true);
		OrderCreationDTO orderWithoutStaff = createOrder("cleaning-suits", false, false);
		OrderCreationDTO orderWithStaff = createOrder("grindery-knifes", true, false);

		timeTableService.rebuildTimeTable();

		Utils.flushAndClear(entityManager);

		Optional<MissMintOrder> optionalOldOrder = orderManager.get(Objects.requireNonNull(oldOrder.getOrder().getId()));
		assertThat(optionalOldOrder).isNotEmpty();
		MissMintOrder updatedOldOrder = optionalOldOrder.get();
		Optional<MissMintOrder> optionalOrderWithoutStaff = orderManager.get(Objects.requireNonNull(orderWithoutStaff.getOrder().getId()));
		assertThat(optionalOrderWithoutStaff).isNotEmpty();
		MissMintOrder updatedOrderWithoutStaff = optionalOrderWithoutStaff.get();
		Optional<MissMintOrder> optionalOrderWithStaff = orderManager.get(Objects.requireNonNull(orderWithStaff.getOrder().getId()));
		assertThat(optionalOrderWithStaff).isNotEmpty();
		MissMintOrder updatedOrderWithStaff = optionalOrderWithStaff.get();

		assertThat(updatedOldOrder.getEntry()).isNull();
		assertThat(updatedOrderWithoutStaff.getEntry()).isNull();
		assertThat(updatedOrderWithStaff.getEntry()).isNotEqualTo(orderWithStaff.getEntry());
		assertThat(Streamable.of(entryRepository.findAll()).get().count()).isEqualTo(1);
	}

	private OrderCreationDTO createOrder(String serviceName, boolean staffHasSkill, boolean isFinished) {
		OrderItem orderItem = itemCatalog.save(new OrderItem("a brave world"));

		Optional<MissMintService> optionalService = serviceCatalog.findByName(serviceName).get().findAny();
		assertThat(optionalService).isNotEmpty();
		MissMintService service = optionalService.get();

		UserAccount userAccount = userAccountManager.create(UUID.randomUUID().toString(), Password.UnencryptedPassword.of("password"));
		Staff staff = new Staff(userAccount, "Aldous", "Huxley", BigDecimal.TEN);
		if (staffHasSkill) {
			staff.addSkill(ServiceManager.getCategory(service));
		}
		staffRepository.save(staff);

		LocalDate date = time.getTime().toLocalDate();
		if (isFinished) {
			date = date.minusDays(10);
		}
		MissMintOrder order = new MissMintOrder(userAccount, "Bob", date, service, orderItem);
		if (isFinished) {
			order.setExpectedFinished(date.plusDays(1));
			order.setFinished(date.plusDays(1));
			order.setOrderState(OrderState.FINISHED);
		}

		TimeTableEntry entry = null;
		if (staffHasSkill && !isFinished) {
			entry = entryRepository.save(timeTableService.createEntry(order));
		}

		return new OrderCreationDTO(orderManager.save(order), staff, entry);
	}

	private static class OrderCreationDTO {
		private final MissMintOrder order;
		private final Staff staff;
		private final TimeTableEntry entry;

		public OrderCreationDTO(MissMintOrder order, Staff staff, TimeTableEntry entry) {
			this.order = order;
			this.staff = staff;
			this.entry = entry;
		}

		public MissMintOrder getOrder() {
			return order;
		}

		public Staff getStaff() {
			return staff;
		}

		public TimeTableEntry getEntry() {
			return entry;
		}
	}
}
