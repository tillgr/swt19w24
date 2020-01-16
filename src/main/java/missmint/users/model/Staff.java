package missmint.users.model;

import missmint.orders.service.ServiceCategory;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Staff member in the system
 */
@Entity
public class Staff {
	@Id
	@GeneratedValue
	private long id;

	@OneToOne
	private UserAccount userAccount;

	private String lastName;
	private String firstName;
	private BigDecimal salary;

	@ElementCollection
	private Set<ServiceCategory> skills = new HashSet<>();

	// empty constructor for Entity
	public Staff() {}

	/**
	 * Create a new staff
	 *
	 * @param userAccount Associated user account
	 * @param firstName Forename of staff
	 * @param lastName Surname of staff
	 * @param salary Salary of the staff
	 */
	public Staff(UserAccount userAccount, String firstName, String lastName, BigDecimal salary) {

		Assert.notNull(userAccount, "UserAccount cannot be null.");
		Assert.hasText(lastName, "Name cannot be null or empty.");
		Assert.hasText(firstName, "Name cannot be null or empty.");
		Assert.notNull(salary, "Salary must not be null.");
		Assert.isTrue(salary.compareTo(BigDecimal.ZERO) >= 0, "Salary must be positive");

		this.userAccount = userAccount;
		this.lastName = lastName;
		this.firstName = firstName;
		this.salary = salary;
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

	public String getLastName() {
		return lastName;
	}

	public String getUserName() {
		return userAccount.getUsername();
	}

	public boolean addSkill(ServiceCategory service) {
		return skills.add(service);
	}

	public void updateSkills(Set<ServiceCategory> service) {
		skills = service;
	}

	public boolean removeSkill(ServiceCategory service) {
		return skills.remove(service);
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public Set<ServiceCategory> getSkills() {
		return skills;
	}

	public Role getRole() {
		return userAccount.getRoles().stream().findFirst().orElseThrow();
	}
}
