package missmint.users.forms;

import missmint.orders.service.Service;
import missmint.orders.service.ServiceCategory;

import javax.validation.constraints.NotBlank;

public class EditStaffForm {
	private final Service newSkill;

	@NotBlank(message = "{EditStaffForm.name.NotBlank}")
	private final String firstName;

	@NotBlank(message = "{EditStaffForm.name.NotBlank}")
	private final String lastName;

	public EditStaffForm(String firstName, String lastName, Service newSkill) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.newSkill = newSkill;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Service getNewSkill() {
		return newSkill;
	}
}
