package missmint.users;

import com.mysema.commons.lang.Assert;
import missmint.users.forms.RegistrationForm;
import missmint.users.model.UserManagement;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ONLY TEMPORARY
 */
@Component
public class TempUserInitializer implements DataInitializer {

	private UserAccountManager accountManager;
	private UserManagement userManagement;

	public TempUserInitializer(UserAccountManager userAccountManager, UserManagement userManagement) {

		Assert.notNull(userAccountManager, "UserAccountManager must not be null");
		Assert.notNull(userManagement, "UserManagement must not be null");

		this.accountManager = userAccountManager;
		this.userManagement = userManagement;
	}

	/**
	 * Initialize user database with dummy accounts for testing purposes
	 */
	@Override
	public void initialize() {

		UserAccount userAccount = accountManager.create("user", Password.UnencryptedPassword.of("test"));
		accountManager.save(userAccount);

		var password = "123";

		List.of(
			new RegistrationForm("Hans","Müller","hans", password),
			new RegistrationForm("Dexter", "Morgan","dextermorgan", password),
			new RegistrationForm("Drax", "The Destroyer", "XXXDestroyerXXX", password)
		).forEach(userManagement::createUserAsEmployee);

	}
}
