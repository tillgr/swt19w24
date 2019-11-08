package missmint.users.model;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class User {

	@Id
	@GeneratedValue
	private long id;

	@OneToOne
	private UserAccount userAccount;

	// empty constructor for Entity
	public User() {}

	public User(UserAccount userAccount) {

		Assert.notNull(userAccount, "UserAccount cannot be null.");

		this.userAccount = userAccount;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public String getUserName() {
		return userAccount.getUsername();
	}

	public long getId() {
		return id;
	}

	public Streamable<Role> getRoles() {
		return userAccount.getRoles();
	}
}
