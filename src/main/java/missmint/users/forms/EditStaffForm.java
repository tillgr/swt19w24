package missmint.users.forms;

import missmint.orders.service.ServiceCategory;

import javax.validation.constraints.NotEmpty;

public class EditStaffForm {
	private final ServiceCategory newSkill;

	@NotEmpty
	private final String firstName;

	@NotEmpty
	private final String lastName;

	public EditStaffForm(String firstName, String lastName, ServiceCategory newSkill) {
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

	public ServiceCategory getNewSkill() {
		return newSkill;
	}
}
