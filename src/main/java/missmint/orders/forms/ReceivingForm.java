package missmint.orders.forms;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ReceivingForm {
	@NotBlank
	private String customer;
	@NotNull
	private Long service;

	public ReceivingForm(String customer, Long service) {
		this.customer = customer;
		this.service = service;
	}

	public String getCustomer() {
		return customer;
	}

	public Long getService() {
		return service;
	}
}
