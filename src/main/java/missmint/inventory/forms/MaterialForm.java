package missmint.inventory.forms;

import org.salespointframework.inventory.InventoryItemIdentifier;

import javax.validation.constraints.*;

public class MaterialForm {

	@NotNull
	@Min(1)
	@Max(10000)
	private int number;

	@NotNull
	private InventoryItemIdentifier materialId;

	public MaterialForm(int number, InventoryItemIdentifier materialId){
		this.materialId = materialId;
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public InventoryItemIdentifier getMaterialId() {
		return materialId;
	}
}
