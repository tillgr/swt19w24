package missmint.users.forms;

import missmint.orders.service.ServiceCategory;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Set;

public class EditStaffForm {
	private final Set<ServiceCategory> updateSkills;

	@NotBlank(message = "{EditStaffForm.name.NotBlank}")
	private final String firstName;

	@NotBlank(message = "{EditStaffForm.name.NotBlank}")
	private final String lastName;

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
