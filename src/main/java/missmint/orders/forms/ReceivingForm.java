package missmint.orders.forms;

import org.salespointframework.catalog.ProductIdentifier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ReceivingForm {
	@NotBlank(message = "{ReceivingForm.customer.NotBlank}")
	private String customer;
	@NotBlank(message = "{ReceivingForm.description.NotBlank}")
	private String description;
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
