package missmint.users.forms;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public class RegistrationForm {

	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String firstName;

	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String lastName;

	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String userName;

	@NotEmpty(message = "{RegistrationForm.password.NotEmpty}")
	private final String password;

	@Digits(integer = Integer.MAX_VALUE, fraction = 2)
	@Min(0)
	private BigDecimal salary;

	public RegistrationForm(String firstName, String lastName, String userName, String password, BigDecimal salary) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.salary = salary;
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

	public BigDecimal getSalary() {
		return salary;
	}
}
