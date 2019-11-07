package missmint.orders.forms;

import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ReceivingForm {
	@NotBlank
	private String customer;
	@NotNull
	private ProductIdentifier service;

	public ReceivingForm(String customer, ProductIdentifier service) {
		this.customer = customer;
		this.service = service;
	}

	public String getCustomer() {
		return customer;
	}

	public ProductIdentifier getService() {
		return service;
	}
}
