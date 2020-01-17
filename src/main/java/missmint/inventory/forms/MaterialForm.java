package missmint.inventory.forms;

import org.salespointframework.inventory.InventoryItemIdentifier;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * A form for changing the amount of stock for a certain material.
 */
public class MaterialForm {
	/**
	 * Amount of stock change.
	 * <p>
	 * Must not be null and between 0 and 10000.
	 */
	@Min(value = 0, message = "{materials.error.low}")
	@Max(value = 10000, message = "{materials.error.high}")
	BigInteger number;
	/**
	 * Identifier of the material for which the amount of stock is changed.
	 */
	@NotNull
	private InventoryItemIdentifier materialId;

	public MaterialForm(BigInteger number, InventoryItemIdentifier materialId) {
		this.number = number;
		this.materialId = materialId;

	}

	public BigInteger getNumber() {
		return number;
	}

	public InventoryItemIdentifier getMaterialId() {
		return materialId;
	}
}
