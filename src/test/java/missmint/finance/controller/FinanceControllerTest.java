package missmint.finance.controller;

import missmint.finance.service.FinanceService;
import missmint.time.TimeService;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FinanceControllerTest {

	@Autowired
	private MockMvc mvc;

	@Value("${general.currency}")
	private String currency;

	@Autowired
	private TimeService timeService;
    @Autowired
	private Accountancy accountancy;
    @Autowired
	private FinanceService financeService;

	private AccountancyEntry accountancyEntry;

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
	@WithMockUser(username = "user", roles = "ADMIN")
	void noFinances() throws Exception {
		AccountancyEntry test = new AccountancyEntry(Money.of(8, currency),  "Test accountancy item");
		accountancy.add(test);
		mvc.perform(get("/finance"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(test.getDescription())))
			.andExpect(content().string(containsString(String.valueOf(test.getValue()))));
	}

	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	void showLastMonthTest() throws Exception {
		AccountancyEntry test = new AccountancyEntry(Money.of(8, currency),  "Test accountancy item");
		accountancy.add(test);
		timeService.forward(Duration.ofDays(33));
		mvc.perform(get("/finance/month"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(test.getDescription())))
			.andExpect(content().string(containsString(String.valueOf(test.getValue()))))
			.andExpect(content().string(containsString(String.valueOf(financeService.getSum(financeService.lastMonth())))));
	}

	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	void financeFormTest() throws Exception {
		mvc.perform(get("/finance/addItem"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("name=\"description\"")))
			.andExpect(content().string(containsString("name=\"price\"")));
	}

	@Test
	@WithMockUser(username = "user", roles = "ADMIN")
	void addFinanceTest() throws Exception {
		AccountancyEntry test = new AccountancyEntry(Money.of(8, currency),  "Test value form");
		mvc.perform(post("/finance/addItem").locale(Locale.ROOT).with(csrf())
			.param("price", String.valueOf(test.getValue().getNumber().intValue()))
			.param("description", test.getDescription()));
		List<AccountancyEntry> list = accountancy.findAll().toList();
		boolean isInList = list
			.stream()
			.anyMatch(entry -> entry.getDescription().equals(test.getDescription()));
		Assert.isTrue(isInList, "accountancy is not found");

	}

	@Test
	void createAccountancy(){
		AccountancyEntry test = new AccountancyEntry(Money.of(10, currency), "first accountancy test");
		accountancy.add(test);
		List<AccountancyEntry> list = accountancy.findAll().toList();
		Assert.isTrue(list.contains(test), "accountancy is not found");
	}



}