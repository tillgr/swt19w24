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
	private MockMvc mvc;
	@Autowired
	private RoomRepository rooms;

	@Test
	@WithMockUser
	void addRoom() throws Exception {

		AddRoomForm form = new AddRoomForm("testraum");
		Room room = form.createRoom();

		rooms.save(room);

		/*
		mvc.perform(get("/rooms/add"))
			.andExpect(status().isOk())
			.andExpect(content().string(not(containsString("Darf nicht leer sein!"))));


		 */
	}

	@Test
	//@WithMockUser
	void deleteRoom() throws Exception {

		/*
		mvc.perform(get(String.format("/rooms/", room.getId())).locale(Locale.ROOT),"/delete")
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Please charge the customer EUR 1.")))
			.andExpect(content().string(not(containsString("compensation"))));


		 */

	}

}
