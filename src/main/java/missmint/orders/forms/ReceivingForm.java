package missmint.orders.forms;

import org.salespointframework.catalog.ProductIdentifier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * A form for the receiving of a customer item for repair.
 */
public class ReceivingForm {
	/**
	 * The name of the customer.
	 *
	 * This value must not be blank.
	 */
	@NotBlank(message = "{ReceivingForm.customer.NotBlank}")
	private String customer;
	/**
	 * A description of the item the customer is giving in for repair.
	 *
	 * This value must not be blank.
	 */
	@NotBlank(message = "{ReceivingForm.description.NotBlank}")
	private String description;
	/**
	 * The identifier of the service this repair is categorised in.
	 *
	 * The identifier must not be null.
	 */
	@NotNull(message = "{ReceivingForm.service.NotNull}")
	private ProductIdentifier service;

	public ReceivingForm(String customer, String description, ProductIdentifier service) {
		this.customer = customer;
		this.description = description;
		this.service = service;
	}

	public String getCustomer() {
		return customer;
	}

	public ProductIdentifier getService() {
		return service;
	}

	public String getDescription() {
		return description;
	}
}
