package missmint.users.forms;

import javax.validation.constraints.NotEmpty;

/**
 * Form is used to create new user accounts
 */
public class RegistrationForm {

	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}")
	private final String name;

	@NotEmpty(message = "{RegistrationForm.password.NotEmpty}")
	private final String password;

	//@NotEmpty(message = "{RegistrationForm.address.NotEmpty}")
	//private final String address;

	public RegistrationForm(String name, String password) {

		this.name = name;
		this.password = password;
		// this.address = address;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	/*public String getAddress() {
		return address;
	}*/
}
