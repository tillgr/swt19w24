package missmint.users.initializer;

import missmint.orders.service.ServiceCategory;
import missmint.users.forms.RegistrationForm;
import missmint.users.model.AccountRole;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
import missmint.users.service.StaffManagement;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Component
public class StaffInitializer implements DataInitializer {

	private UserAccountManager userAccountManager;
	private StaffManagement staffManagement;
	private StaffRepository staffRepository;

	public StaffInitializer(UserAccountManager userAccountManager, StaffManagement staffManagement, StaffRepository staffRepository) {
		this.staffRepository = staffRepository;

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
			new RegistrationForm("Hans","Müller","hans", password),
			new RegistrationForm("Dexter", "Morgan","dextermorgan", password),
			new RegistrationForm("Drax", "The Destroyer", "XXXDestroyerXXX", password)
		).forEach(staffManagement::createStaff);

		Optional<Staff> optionalStaff = staffManagement.findStaffByUserName("hans");
		Assert.isTrue(optionalStaff.isPresent(), "hans was not created");
		Staff staff = optionalStaff.get();
		staff.addSkill(ServiceCategory.KLUDGE);
		staffRepository.save(staff);
	}
}
