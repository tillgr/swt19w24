package missmint.users.service;

import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Transactional
public class SalaryServiceTests {
	@Autowired
	private UserAccountManager userAccountManager;
	@Autowired
	private StaffRepository staffRepository;
	@Autowired
	private SalaryService salaryService;
	@Autowired
	private Accountancy accountancy;
	@Autowired
	private BusinessTime time;
	@Value("${general.currency}")
	private String currency;

	@Test
	void noTimeElapsed() {
		Staff staff = createStaff(100);

		salaryService.payStaff(Duration.ZERO);

		assertThat(accountancy.findAll().filter(accountancyEntry ->
			accountancyEntry.getDescription().contains(staff.getLastName())
		).get().count()).isEqualTo(0);
	}

	@Test
	void paySalary() {
		Staff staff1 = createStaff(100);
		Staff staff2 = createStaff(10);

		LocalDateTime now = time.getTime();
		time.forward(Duration.between(now, now.with(TemporalAdjusters.lastDayOfMonth())));
		salaryService.payStaff(Duration.ofDays(70));

		assertThat(accountancy.findAll().filter(accountancyEntry ->
			accountancyEntry.getDescription().contains(staff1.getLastName())
		).map(accountancyEntry -> {
			assertThat(accountancyEntry.getValue().isEqualTo(Money.of(staff1.getSalary(), currency)));
			return accountancyEntry;
		}).get().count()).isEqualTo(2);
		assertThat(accountancy.findAll().filter(accountancyEntry ->
			accountancyEntry.getDescription().contains(staff2.getLastName())
		).map(accountancyEntry -> {
			assertThat(accountancyEntry.getValue().isEqualTo(Money.of(staff2.getSalary(), currency)));
			return accountancyEntry;
		}).get().count()).isEqualTo(2);
	}

	private Staff createStaff(long salary) {
		UserAccount userAccount = userAccountManager.create(UUID.randomUUID().toString(), Password.UnencryptedPassword.of("password"));
		Staff staff = new Staff(userAccount, UUID.randomUUID().toString(), UUID.randomUUID().toString(), BigDecimal.valueOf(salary));
		return staffRepository.save(staff);
	}
}
