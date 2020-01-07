package missmint.users.forms;

import javax.validation.constraints.NotBlank;

public class PasswordForm {
	@NotBlank(message = "{PasswordForm.password.NotBlank}")
	private final String password;

	public PasswordForm(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
}
