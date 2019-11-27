package missmint.inventory.service;


import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

import java.util.Iterator;

public class InventoryManagement {
	private final UniqueInventory<UniqueInventoryItem> materialInventory;

	InventoryManagement(UniqueInventory<UniqueInventoryItem> materialInventory) {
		this.materialInventory = materialInventory;
	}

	private UniqueInventory<UniqueInventoryItem> getMaterialInventory() {
		return materialInventory;
	}

	public void restock(){

		Iterator<UniqueInventoryItem> it = materialInventory.findAll().iterator();
		for (int i = 0; i < 7; i++) {
			UniqueInventoryItem currentItem = it.next();
			if (quantity.isGreaterThanOrEqualTo(Quantity.of(0))) {
				getMaterialInventory().findById(materialId).ifPresent(itQM ->
					getMaterialInventory().save(itQM.increaseQuantity(Quantity.of(finalNumber, metric)))
				);
			}
		}
	}

}
