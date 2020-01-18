package missmint.users.initializer;

import missmint.orders.service.ServiceCategory;
import missmint.users.forms.RegistrationForm;
import missmint.users.model.AccountRole;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
import missmint.users.service.StaffManagement;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Prepopulate the system with an admin account and a few staff member for demonstration.
 */
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
		var admin = "user";

		if (staffManagement.findByUserName(admin).isPresent()) {
			return;
		}

		if(userAccountManager.findByUsername(admin).isEmpty()) {
			staffManagement.createStaff(new RegistrationForm(
							"Kevin",
							"Becker",
							admin,
							"test",
							BigDecimal.valueOf(200)),
					Role.of(AccountRole.ADMIN.name())
			);
		}

		List.of(
			new RegistrationForm("Hans","Müller","hans", password, BigDecimal.valueOf(100)),
			new RegistrationForm("Dexter", "Morgan","dextermorgan", password, BigDecimal.valueOf(50)),
			new RegistrationForm("Drax", "The Destroyer", "XXXDestroyerXXX", password, BigDecimal.valueOf(20))
		).forEach(form -> {
			if (userAccountManager.findByUsername(form.getUserName()).isEmpty()) {
				staffManagement.createStaff(form);
			}
		});

		Optional<Staff> optionalStaff = staffManagement.findStaffByUserName("hans");
		Assert.isTrue(optionalStaff.isPresent(), "hans was not created");
		Staff staff = optionalStaff.get();
		staff.addSkill(ServiceCategory.KLUDGE);
		staffRepository.save(staff);
	}
}
