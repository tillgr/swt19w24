package missmint.users.service;

import missmint.users.forms.RegistrationForm;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Transactional
public class StaffManagementTest {

	@Autowired
	private StaffManagement staffManagement;

	@Autowired
	private UserAccountManager userAccountManager;

	@Autowired
	private StaffRepository staffRepository;

	@Test
	void createNewStaff() {
		var userName = "user_123";
		var form = new RegistrationForm("Le", "me", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		var user = userAccountManager.findByUsername(userName);
		assertTrue(user.isPresent());
		assertTrue(staffRepository.findByUserAccount(user.orElseThrow()).isPresent());
	}

	@Test
	void nullRegistrationForm() {
		assertThatThrownBy(() -> staffManagement.createStaff(null)).isNotNull();
	}

	@Test
	void deleteExistingStaff() {
		var userName = "user_123";
		var form = new RegistrationForm("Le", "me", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		var user = userAccountManager.findByUsername(userName).orElseThrow();
		var staff = staffRepository.findByUserAccount(user).orElseThrow();
		staffManagement.deleteStaff(user.getUsername());

		assertTrue(userAccountManager.findByUsername(userName).isEmpty());
		assertTrue(staffRepository.findById(staff.getId()).isEmpty());
	}

	@Test
	void deleteStaffByEmptyName() {
		assertThatThrownBy(() -> staffManagement.deleteStaff(null)).isNotNull();
	}

	@Test
	void changePassword() {
		String userName = "user_123";
		RegistrationForm form = new RegistrationForm("Le", "me", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		Staff staff = staffManagement.findStaffByUserName(userName).orElseThrow();
		Password.EncryptedPassword password = staff.getUserAccount().getPassword();

		staffManagement.changePassword(staff, "321");
		assertThat(staff.getUserAccount().getPassword()).isNotEqualTo(password);
	}

	@Test
	void changePasswordNullStaff() {
		assertThatThrownBy(() -> staffManagement.changePassword(null, "321")).isNotNull();
	}

	@Test
	void changePasswordEmptyPassword() {
		String userName = "user_123";
		RegistrationForm form = new RegistrationForm("Le", "me", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		Staff staff = staffManagement.findStaffByUserName(userName).orElseThrow();

		assertThatThrownBy(() -> staffManagement.changePassword(staff, " \t  ")).isNotNull();
	}
}
