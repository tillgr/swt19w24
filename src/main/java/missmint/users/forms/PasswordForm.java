package missmint.users.forms;

import javax.validation.constraints.NotEmpty;

public class PasswordForm {
	@NotEmpty(message = "{PasswordForm.password.NotEmpty}")
	private final String password;

	public PasswordForm(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
}
