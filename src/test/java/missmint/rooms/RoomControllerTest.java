package missmint.rooms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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

		long roomsWithTestName = Streamable.of(rooms.findAll()).filter(r -> r.getName().equals("testRaum")).get().count();
		assertThat(roomsWithTestName).isEqualTo(1);
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
