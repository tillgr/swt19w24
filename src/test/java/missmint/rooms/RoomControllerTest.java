package missmint.rooms;

import missmint.inventory.products.OrderItem;
import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderState;
import missmint.orders.service.MissMintService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest {
	@Autowired
	private RoomRepository rooms;

	@Autowired
	private MockMvc mvc;

	@Test
	@WithMockUser
	void addRoom() throws Exception {

		mvc.perform(post("/rooms/addRoom").locale(Locale.ROOT).with(csrf())
				.param("name", "testRaum")
		)
				.andExpect(status().isOk())
				.andExpect(view().name("rooms"))
				.andExpect(content().string(containsString("testRaum")));
	}

	@Test
	@WithMockUser
	void deleteRoom() throws Exception {
	Room room = new Room("testRaum");

		mvc.perform(post(String.format("/rooms/%s/deleteRoom", room.getId())).locale(Locale.ROOT).with(csrf())
				.param("name", "testRaum")
		)
				.andExpect(status().isOk())
				.andExpect(view().name("rooms"))
				.andExpect(content().string(not(containsString(String.valueOf(room.getId())))));
	}



}
