package missmint.users.forms;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * Form for the registration of new staff member
 */
public class RegistrationForm {

	/**
	 * Forename of staff
	 */
	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String firstName;

	/**
	 * Surname of staff
	 */
	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String lastName;

	/**
	 * Username of staff
	 */
	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String userName;

	/**
	 * Password for login
	 */
	@NotBlank(message = "{RegistrationForm.password.NotBlank}")
	private final String password;

	/**
	 * Salary per month
	 */
	@Digits(integer = Integer.MAX_VALUE, fraction = 2)
	@Min(value = 0, message = "{EditStaffForm.salary.NotNegative}")
	// maximum value for the data type used in the database
	@Max(value = 999999999999999L, message = "{EditStaffForm.salary.TooLarge}")
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
