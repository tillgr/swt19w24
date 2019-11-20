package missmint.users.initializer;

import missmint.users.forms.RegistrationForm;
import missmint.users.model.AccountRole;
import missmint.users.service.StaffManagement;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class StaffInitializer implements DataInitializer {

	private UserAccountManager userAccountManager;
	private StaffManagement staffManagement;

	public StaffInitializer(UserAccountManager userAccountManager, StaffManagement staffManagement) {

		Assert.notNull(userAccountManager, "UserAccountManager must not be null");
		Assert.notNull(staffManagement, "UserManagement must not be null");

		this.userAccountManager = userAccountManager;
		this.staffManagement = staffManagement;
	}

	@Override
	public void initialize() {

		var password = "123";

		var adminRole = Role.of(AccountRole.ADMIN.name());
		userAccountManager.create("user", Password.UnencryptedPassword.of("test"), adminRole);

		List.of(
			new RegistrationForm("Hans","Müller","hans", password, AccountRole.EMPLOYEE.name()),
			new RegistrationForm("Dexter", "Morgan","dextermorgan", password, AccountRole.EMPLOYEE.name()),
			new RegistrationForm("Drax", "The Destroyer", "XXXDestroyerXXX", password, AccountRole.EMPLOYEE.name())
		).forEach(staffManagement::createStaff);
	}
}
