package missmint.inventory.forms;

import org.salespointframework.inventory.InventoryItemIdentifier;

import javax.validation.constraints.*;

/**
 * A form for changing the amount of stock for a certain material.
 */
public class MaterialForm {
	/**
	 * Amount of stock change.
	 *
	 * Must not be null and between 1 and 10000.
	 */
	@NotNull(message = "{MaterialForm.number.notNull}")
	@Min(1)
	@Max(10000)
	private int number;
	/**
	 * Identifier of the material for which the amount of stock is changed.
	 */
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
