package missmint.users.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StaffControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	void unauthenticated() throws Exception {
		mvc.perform(get("/users"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
		mvc.perform(get("/users/registration"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(username = "hans", roles = {"EMPLOYEE"})
	void authenticatedAsEmployee() throws Exception {
		mvc.perform(get("/users"))
				.andExpect(status().isForbidden());
		mvc.perform(get("/users/registration"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Boss", roles = {"ADMIN"})
	void authenticatedAsAdmin() throws Exception {
		mvc.perform(get("/users"))
				.andExpect(status().isOk());
		mvc.perform(get("/users/registration"))
				.andExpect(status().isOk());
	}
}
