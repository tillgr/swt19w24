package missmint.users.forms;

import missmint.orders.service.ServiceCategory;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Form for editing staff
 */
public class EditStaffForm {
	/**
	 * Set of updated skills represented by ServiceCategories
	 */
	private final Set<ServiceCategory> updateSkills;

	/**
	 * forename of the staff
	 */
	@NotBlank(message = "{EditStaffForm.name.NotBlank}")
	private final String firstName;

	/**
	 * Surname of the staff
	 */
	@NotBlank(message = "{EditStaffForm.name.NotBlank}")
	private final String lastName;

	/**
	 * Salary per month
	 */
	@Digits(integer = Integer.MAX_VALUE, fraction = 2)
	@Min(value = 0, message = "{EditStaffForm.salary.NotNegative}")
	// maximum value for the data type used in the database
	@Max(value = 999999999999999L, message = "{EditStaffForm.salary.TooLarge}")
	private BigDecimal salary;

	public EditStaffForm(String firstName, String lastName, Set<ServiceCategory> updateSkills, BigDecimal salary) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.updateSkills = updateSkills;
		this.salary = salary;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Set<ServiceCategory> getUpdateSkills() {
		return updateSkills;
	}

	public BigDecimal getSalary() {
		return salary;
	}
}
