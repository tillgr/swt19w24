package missmint.users.model;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.util.Assert;

import javax.persistence.Entity;

/**
 * Employee working in the shop.
 * Has specific skill set.
 */
@Entity
public class Staff extends User implements Resource {

	private String lastName;
	private String firstName;

	// empty constructor for Entity
	public Staff() {}

	public Staff(UserAccount userAccount, String firstName, String lastName) {

		super(userAccount);

		Assert.hasText(lastName, "Name cannot be null or empty.");
		Assert.hasText(firstName, "Name cannot be null or empty.");

		this.lastName = lastName;
		this.firstName = firstName;
	}

	/**
	 * Returns last name for now.
	 *
	 * @return last name
	 */
	@Override
	public String getName() {
		return lastName;
	}

	/**
	 * Returns the first name of the staff member
	 * @return first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Calculates the monthly salary
	 * @return montly salary
	 */
	public Double calculateSalary() {
		// TODO: implement salary calulation
		return 0.0;
	}
}
