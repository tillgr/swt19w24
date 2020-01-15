package missmint.users.forms;

import javax.validation.constraints.NotBlank;

/**
 * Form for changing password
 */
public class PasswordForm {

	/**
	 * New password for the staff
	 */
	@NotBlank(message = "{PasswordForm.password.NotBlank}")
	private final String password;

	public PasswordForm(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
}
