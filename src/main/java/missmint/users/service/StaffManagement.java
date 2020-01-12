package missmint.users.service;

import missmint.Utils;
import missmint.orders.service.ServiceCategory;
import missmint.time.EntryRepository;
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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class StaffManagement {

	private final StaffRepository staffRepository;
	private final UserAccountManager userAccountManager;
	private final EntryRepository entryRepository;

	public StaffManagement(
			StaffRepository staffRepository,
			UserAccountManager userAccountManager,
			EntryRepository entryRepository
	) {

		Assert.notNull(staffRepository, "StaffRepository must not be null");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null");
		Assert.notNull(entryRepository, "EntryRepository must not be null");

		this.staffRepository = staffRepository;
		this.userAccountManager = userAccountManager;
		this.entryRepository = entryRepository;
	}

	/**
	 * Creates a new user with employee/admin role and saves it in the system
	 *
	 * @param form used to create account credentials
	 */
	public void createStaff(RegistrationForm form, Role role) {

		Assert.notNull(form, "RegistrationForm cannot be null.");
		var password = Password.UnencryptedPassword.of(form.getPassword());
		var userAccount = userAccountManager.create(form.getUserName(), password, role);

		staffRepository.save(new Staff(userAccount, form.getFirstName(), form.getLastName(), form.getSalary()));
	}

	public void createStaff(RegistrationForm form) {
		createStaff(form , Role.of(AccountRole.EMPLOYEE.name()));
	}

	/**
	 * Delete the user from the UserAccountManager and from
	 * the StaffRepository
	 *
	 * @param userName of the UserAccount
	 */
	public void deleteStaff(String userName) {

		Assert.notNull(userName, "UserName cannot be null.");

		var user = Utils.getOrThrow(userAccountManager.findByUsername(userName));
		var staff = Utils.getOrThrow(staffRepository.findByUserAccount(user));
		var id = staff.getId();
		var entriesByStaff = entryRepository.findAllByStaff(staff);
		entriesByStaff.forEach(timeTableEntry -> timeTableEntry.getOrder().setEntry(null));
		entryRepository.deleteTimeTableEntriesByStaff(staff);
		staffRepository.deleteById(id);
		userAccountManager.delete(user);
	}

	public Optional<UserAccount> findByUserName(String userName) {
		return userAccountManager.findByUsername(userName);
	}

	public Optional<Staff> findStaffByUserName(String userName) {
		return findByUserName(userName).flatMap(staffRepository::findByUserAccount);
	}

	public void editStaff(Staff staff, String firstName, String lastName, BigDecimal salary, Set<ServiceCategory> service) {
		staff.setFirstName(firstName);
		staff.setLastName(lastName);
		staff.setSalary(salary);
		if (service != null) {
			staff.updateSkills(service);
		} else {
			staff.updateSkills(new HashSet<>());
		}
		staffRepository.save(staff);
	}

	public Iterable<Staff> getAllStaff() {
		return staffRepository.findAll();
	}

	/**
	 * Changes the password of the staff member and saves the changes to the database.
	 *
	 * @param staff the staff member to edit
	 * @param password the password to change to change the staff's password to
	 */
	public void changePassword(Staff staff, String password) {
		Assert.notNull(staff, "staff member should not be null");

		userAccountManager.changePassword(staff.getUserAccount(), Password.UnencryptedPassword.of(password));
	}
}
