package missmint.time;

import org.junit.jupiter.api.Test;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class TimeControllerTests {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private BusinessTime time;

	@Test
	void unauthenticated() throws Exception {
		mvc.perform(post("/forward"))
			.andExpect(status().is4xxClientError());
	}

	@Test
	@WithMockUser()
	void forward() throws Exception {
		LocalDateTime previous = time.getTime();

		mvc.perform(post("/forward").with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"));

		assertThat(Duration.between(previous, time.getTime())).isBetween(Duration.ofDays(1), Duration.ofDays(1).plusSeconds(10));
	}

}
