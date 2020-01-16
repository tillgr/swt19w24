package missmint;

import org.junit.jupiter.api.Test;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.temporal.ChronoField;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
public class IndexControllerTests {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private BusinessTime businessTime;

	@Test
	void unauthenticated() throws Exception {
		mvc.perform(get("/").locale(Locale.ROOT))
				.andExpect(content().string(containsString("Please log in to use the management software.")));
	}

	@Test
	@WithMockUser
	void time() throws Exception {
		mvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(String.valueOf(businessTime.getTime().get(ChronoField.YEAR)))))
				.andExpect(content().string(containsString(String.valueOf(businessTime.getTime().get(ChronoField.MONTH_OF_YEAR)))))
				.andExpect(content().string(containsString(String.valueOf(businessTime.getTime().get(ChronoField.DAY_OF_MONTH)))));
	}
}
