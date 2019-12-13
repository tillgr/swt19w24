package missmint.users.model;

import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Transactional
public class StaffTest {
	@Autowired
	private UserAccountManager userAccountManager;

	@Test
	void createNewStaff() {
		var userName = "tester";
		var password = Password.UnencryptedPassword.of("password");
		var userAccount = userAccountManager.create(userName, password);

		assertDoesNotThrow(
				() -> new Staff(userAccount, "tester", "test", BigDecimal.valueOf(0))
		);
	}

	@Test
	void nullUserAccount() {
		assertThatThrownBy(
				() -> new Staff(null, "tester", "adlk", BigDecimal.valueOf(1.0))
		).isNotNull();
	}

	@Test
	void blankFirstName() {
		var userName = "tester";
		var password = Password.UnencryptedPassword.of("password");
		var userAccount = userAccountManager.create(userName, password);

		assertThatThrownBy(
				() -> new Staff(userAccount, "", "adlk", BigDecimal.valueOf(1.0))
		).isNotNull();

		assertThatThrownBy(
				() -> new Staff(userAccount, " ", "adlk", BigDecimal.valueOf(1.0))
		).isNotNull();
	}

	@Test
	void blankLastName() {
		var userName = "tester";
		var password = Password.UnencryptedPassword.of("password");
		var userAccount = userAccountManager.create(userName, password);

		assertThatThrownBy(
				() -> new Staff(userAccount, "a", "", BigDecimal.valueOf(1.0))
		).isNotNull();

		assertThatThrownBy(
				() -> new Staff(userAccount, "a", " ", BigDecimal.valueOf(1.0))
		).isNotNull();
	}

	@Test
	void nullSalary() {
		var userName = "tester";
		var password = Password.UnencryptedPassword.of("password");
		var userAccount = userAccountManager.create(userName, password);

		assertThatThrownBy(
				() -> new Staff(userAccount, "a", "b", null)
		).isNotNull();
	}

	@Test
	void negativeSalary() {
		var userName = "tester";
		var password = Password.UnencryptedPassword.of("password");
		var userAccount = userAccountManager.create(userName, password);

		assertThatThrownBy(
				() -> new Staff(userAccount, "a", "b", BigDecimal.valueOf(-0.01))
		).isNotNull();
	}
}
