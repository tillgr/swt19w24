package missmint.users.forms;

import javax.validation.constraints.NotEmpty;

/**
 * Form is used to create new user accounts
 */
public class RegistrationForm {

	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}")
	private final String firstName;

	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}")
	private final String lastName;

	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}")
	private final String userName;

	@NotEmpty(message = "{RegistrationForm.password.NotEmpty}")
	private final String password;

	//@NotEmpty(message = "{RegistrationForm.address.NotEmpty}")
	//private final String address;

	public RegistrationForm(String firstName, String lastName, String userName, String password) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		// this.address = address;
	}


	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	/*public String getAddress() {
		return address;
	}*/
}
