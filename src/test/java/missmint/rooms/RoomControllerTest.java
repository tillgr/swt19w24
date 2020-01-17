package missmint.rooms;

import missmint.Utils;
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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;

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
		Room room = new Room("testRaum");
		rooms.save(room);

		mvc.perform(post("/rooms/add").locale(Locale.ROOT).with(csrf())
		)
				.andExpect(status().isOk())
				.andExpect(view().name("rooms"))
				.andExpect(content().string(containsString("testRaum")));

	}

	@Test
	@WithMockUser
	void roomExists() throws Exception {
		Room room = new Room("testRaum");
		rooms.save(room);
		Set<String> names= new HashSet<>();

		mvc.perform(post("/rooms/add").locale(Locale.ROOT).with(csrf())
				.param("name", "testRaum")
		)
				.andExpect(status().isOk())
				.andExpect(view().name("rooms"))
				.andExpect(content().string(containsString("Room already exists!")));

		rooms.findAll().forEach(room1 -> names.add(room1.getName()));
		System.out.println(names);
		System.out.println(rooms.count());

		//assertThat( Long.valueOf(names.size()) == (Long)rooms.count()).isTrue();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteRoom() throws Exception {
		Room room = new Room("testRaum");
		rooms.save(room);
		rooms.delete(room);

		mvc.perform(post(String.format("/rooms/%s/delete", room.getId())).locale(Locale.ROOT).with(csrf())
		)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/rooms"))
				.andExpect(content().string(not(containsString("testRaum"))));

	}

}
