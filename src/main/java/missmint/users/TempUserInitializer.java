package missmint.users;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Component;

/**
 * ONLY TEMPORARY
 */
@Component
public class TempUserInitializer implements DataInitializer {

	private UserAccountManager accountManager;

	public TempUserInitializer(UserAccountManager userAccountManager) {
		this.accountManager = userAccountManager;
	}

	@Override
	public void initialize() {
		UserAccount userAccount = accountManager.create("user", Password.UnencryptedPassword.of("test"));
		accountManager.save(userAccount);
	}
}
