package missmint.inventory.forms;

import org.salespointframework.catalog.ProductIdentifier;

import javax.validation.constraints.NotNull;

public class OrderItemForm {

	@NotNull
	private ProductIdentifier orderItemId;

	public OrderItemForm(ProductIdentifier orderItemId){
		this.orderItemId = orderItemId;
	}

	public ProductIdentifier getOrderItemId() {
		return orderItemId;
	}
}
