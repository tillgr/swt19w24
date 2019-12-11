package missmint.rooms;

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
public class RoomTest {
	//TODO
}
