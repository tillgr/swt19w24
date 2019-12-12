package missmint.finance.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FinanceControllerTest {

	@Autowired
	private MockMvc mvc;

	//@Autowired
	//private FinanceService financeService;

	//@Autowired
	//private Accountancy accountancy;

	@Test
	void notAdmin() throws Exception {
		mvc.perform(get("/finance"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));

		mvc.perform(get("/finance/addItem"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	void noFinances() throws Exception {
		mvc.perform(get("/finance"))
			.andExpect(status().isOk());
	}

}