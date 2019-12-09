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
public class InventoryControllerTest {

		@Autowired
		MockMvc mvc;

		@Test
		void preventsPublicAccess() throws Exception {

			mvc.perform(get("/material"))
					.andExpect(status().isFound())
					.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));

			mvc.perform(get("/material/consume"))
					.andExpect(status().is(405));

			mvc.perform(get("/material/restock"))
					.andExpect(status().is(405));
		}

		@Test
		@WithMockUser(username = "user", roles = "ADMIN")
		void accessibleForAdmin() throws Exception {

			mvc.perform(get("/material")) //
					.andExpect(status().isOk()) //
					.andExpect(model().attributeExists("material"));


		}

}
