package missmint.users.forms;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class RegistrationForm {

	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String firstName;

	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String lastName;

	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String userName;

	@NotBlank(message = "{RegistrationForm.password.NotBlank}")
	private final String password;

	@Digits(integer = Integer.MAX_VALUE, fraction = 2)
	@Min(0)
	@Max(999999999999999L) // maximum value for the data type used in the database
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
