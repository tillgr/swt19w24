package missmint.users.service;

import missmint.orders.service.ServiceCategory;
import missmint.users.forms.RegistrationForm;
import missmint.users.model.AccountRole;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@Transactional
public class StaffManagement {

	private final StaffRepository staffRepository;
	private final UserAccountManager userAccountManager;

	public StaffManagement(StaffRepository staffRepository, UserAccountManager userAccountManager) {

		Assert.notNull(staffRepository, "StaffRepository must not be null");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null");

		this.staffRepository = staffRepository;
		this.userAccountManager = userAccountManager;
	}

	/**
	 * Creates a new user with employee/admin role and saves it in the system
	 *
	 * @param form used to create account credentials
	 * @return User
	 */
	public Staff createStaff(RegistrationForm form) {

		Assert.notNull(form, "RegistrationForm cannot be null.");

		var role = Role.of(AccountRole.EMPLOYEE.name());

		var password = Password.UnencryptedPassword.of(form.getPassword());
		var userAccount = userAccountManager.create(form.getUserName(), password, role);

		return staffRepository.save(new Staff(userAccount, form.getFirstName(), form.getLastName()));
	}

	/**
	 * Delete the user from the UserAccountManager and from
	 * the StaffRepository
	 *
	 * @param userName of the UserAccount
	 * @param id of the UserAccount
	 */
	public void deleteStaff(String userName, Long id) {

		var userOptional = userAccountManager.findByUsername(userName);

		userOptional.ifPresent( user -> {
			staffRepository.deleteById(id);
			userAccountManager.delete(user);
		});

	}

	public Optional<UserAccount> findByUserName(String userName) {
		return userAccountManager.findByUsername(userName);
	}

	public Optional<Staff> findStaffByUserName(String userName) {
		return findByUserName(userName).flatMap(staffRepository::findByUserAccount);
	}

	public Optional<Staff> findStaffById(Long id) {
		return staffRepository.findById(id);
	}

	public void editStaff(Long id, String firstName, String lastName, ServiceCategory service) {

		staffRepository.findById(id).ifPresent(staff -> {
			staff.setFirstName(firstName);
			staff.setLastName(lastName);
			if (service != null) {
				staff.addSkill(service);
			}
			staffRepository.save(staff);
		});
	}

	public Iterable<Staff> getAllStaff() {
		return staffRepository.findAll();
	}
}
