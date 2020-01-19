package missmint.time;

import missmint.inventory.products.OrderItem;
import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderService;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceCategory;
import missmint.rooms.Room;
import missmint.rooms.RoomRepository;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class TimeTableEntryTest {

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

	TimeTableEntry entry;
	Staff staff;
	MissMintOrder order;

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


	void createEntry(){
		order = createOrder();
		staff = createStaff();
		Room room = createRoom();
		LocalDate now = time.getTime().toLocalDate();

		entry = new TimeTableEntry(order, staff, 1, room, now);
	}

	@Test
	void getId(){
		createEntry();
		assertThat(entry.getId()).isNotNull();
	}

	@Test
	void getOrder(){
		createEntry();
		assertThat(entry.getOrder()).isEqualTo(order);
	}

	@Test
	void getDate(){
		createEntry();
		assertThat(entry.getDate()).isEqualTo(time.getTime().toLocalDate());
	}

	@Test
	void getBeginning(){
		//TODO
		createEntry();
		assertThat(entry.getBeginning()).isEqualTo(LocalDateTime.of(time.getTime().toLocalDate(), TimeTableService.SLOTS.get(1).getFirst()));
	}

	@Test
	void getEnd(){
		createEntry();
		assertThat(entry.getEnd()).isEqualTo(LocalDateTime.of(time.getTime().toLocalDate(), TimeTableService.SLOTS.get(1).getSecond()));
	}

	@Test
	void getStaff(){
		//TODO
		createEntry();
		assertThat(entry.getStaff()).isEqualTo(staff);
	}

	@Test
	void equals(){
		//TODO
		createEntry();
		assertThat(entry.equals(entry)).isTrue();
	}

	@Test
	void hashCodeTest(){
		//TODO
		createEntry();
		assertThat(entry.hashCode()).isEqualTo(Objects.hash(entry.getId()));
	}

}
