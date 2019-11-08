package missmint.users.model;

import missmint.users.forms.RegistrationForm;
import missmint.users.repositories.UserRepository;
import missmint.users.roles.AccountRole;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Manages user accounts
 */
@Service
@Transactional
public class UserManagement {

	private final UserRepository userRepository;
	private final UserAccountManager userAccountManager;

	public UserManagement(UserRepository userRepository, UserAccountManager userAccountManager) {

		Assert.notNull(userRepository, "UserRepository must not be null");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null");

		this.userRepository = userRepository;
		this.userAccountManager = userAccountManager;
	}

	/**
	 * Creates a new user with employee role and saves it in the system
	 * @param form used to create account credentials
	 * @return User
	 */
	public User createUserAsEmployee(RegistrationForm form) {

		Assert.notNull(form, "RegistrationForm cannot be null.");
		var role = Role.of(AccountRole.EMPLOYEE.toString());

		var password = Password.UnencryptedPassword.of(form.getPassword());
		var userAccount = userAccountManager.create(form.getUserName(), password, role);

		return userRepository.save(new Staff(userAccount, form.getFirstName(), form.getLastName()));
	}

	public Iterable<Staff> getAllUsers() {
		return userRepository.findAll();
	}
}
