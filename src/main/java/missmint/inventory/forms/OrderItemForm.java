package missmint.inventory.forms;

import org.salespointframework.catalog.ProductIdentifier;

public class OrderItemForm {

	private ProductIdentifier orderItemId;

	public OrderItemForm(ProductIdentifier orderItemId){
		this.orderItemId = orderItemId;
	}

	public ProductIdentifier getOrderItemId() {
		return orderItemId;
	}
}
