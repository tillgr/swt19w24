package missmint.users.model;

import missmint.orders.service.ServiceCategory;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Staff {

	@Id
	@GeneratedValue
	private long id;

	@OneToOne
	private UserAccount userAccount;

	private String lastName;
	private String firstName;

	@ElementCollection
	private Set<ServiceCategory> skills = new HashSet<>();

	// empty constructor for Entity
	public Staff() {}

	public Staff(UserAccount userAccount, String firstName, String lastName) {

		Assert.notNull(userAccount, "UserAccount cannot be null.");
		Assert.hasText(lastName, "Name cannot be null or empty.");
		Assert.hasText(firstName, "Name cannot be null or empty.");

		this.userAccount = userAccount;
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public long getId() {
		return id;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String lastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getUserName() {
		return userAccount.getUsername();
	}

	public boolean addSkill(ServiceCategory service) {
		return skills.add(service);
	}

	public boolean removeSkill(ServiceCategory service) {
		return skills.remove(service);
	}

	public Set<ServiceCategory> getSkills() {
		return skills;
	}

	public Double calculateSalary() {
		// TODO: implement salary calculation
		return 0.0;
	}
}
