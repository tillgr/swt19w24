package missmint.orders.forms;

import javax.validation.constraints.NotBlank;

public class ReceivingForm {
	@NotBlank
	private String customer;
	private long service;

	public String getCustomer() {
		return customer;
	}

	public long getService() {
		return service;
	}
}
