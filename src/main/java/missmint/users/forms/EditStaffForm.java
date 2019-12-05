package missmint.users.forms;

import missmint.orders.service.ServiceCategory;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class EditStaffForm {
	private final ServiceCategory newSkill;

	@NotBlank(message = "{EditStaffForm.name.NotBlank}")
	private final String firstName;

	@NotBlank(message = "{EditStaffForm.name.NotBlank}")
	private final String lastName;

	@Digits(integer = Integer.MAX_VALUE, fraction = 2)
	@Min(0)
	private BigDecimal salary;

	public EditStaffForm(String firstName, String lastName, ServiceCategory newSkill, BigDecimal salary) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.newSkill = newSkill;
		this.salary = salary;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public ServiceCategory getNewSkill() {
		return newSkill;
	}

	public BigDecimal getSalary() {
		return salary;
	}
}
