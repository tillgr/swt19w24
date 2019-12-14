package missmint.rooms;

import missmint.inventory.products.OrderItem;
import missmint.orders.order.MissMintOrder;
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
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
public class RoomTest {

	@Test
	@Autowired
	void canNotPickUp() throws Exception {
		MissMintOrder order = createOrder();
		orderManager.save(order);

		mvc.perform(get(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT))
				.andExpect(status().isBadRequest());

		mvc.perform(post(String.format("/orders/pickup/%s", order.getId())).locale(Locale.ROOT).with(csrf()))
				.andExpect(status().isBadRequest());
	}

}
