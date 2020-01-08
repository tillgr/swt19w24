package missmint.inventory.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderItemControllerTest {
	@Autowired
	MockMvc mvc;
	@Test
	void preventsPublicAccess() throws Exception {
		mvc.perform(get("/orderItem"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}
	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	void AccessibleForAdmin() throws Exception {
		mvc.perform(get("/orderItem"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("orders"));
	}
}
