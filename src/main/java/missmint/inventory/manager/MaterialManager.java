package missmint.inventory.manager;

import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class MaterialManager {

	private final UniqueInventory<UniqueInventoryItem> materialInventory;

	public MaterialManager(UniqueInventory<UniqueInventoryItem> materialInventory) {
		this.materialInventory = materialInventory;
	}

	public UniqueInventory<UniqueInventoryItem> getMaterialInventory() {
		return materialInventory;
	}


	public void restock(InventoryItemIdentifier materialId, String sQuantity, int number){

		String newQ = "";

		for (int i = 0; i < sQuantity.length(); i++) {
			String test = "" + sQuantity.charAt(i);
			if(!Pattern.matches("\\d+", test)){
				break;
			}
			newQ += sQuantity.charAt(i);
		}

		int iQuantity = Integer.parseInt(newQ);
		Quantity quantity = Quantity.of(iQuantity);
		Metric metric = getMaterialInventory().findById(materialId).get().getQuantity().getMetric();

		int max_quantity = 900;
		int max_addable_quantity = max_quantity - (quantity.getAmount().intValueExact());
		int old_quantity = getMaterialInventory().findById(materialId).get().getQuantity().getAmount().intValueExact();
		int new_quantity = old_quantity + number;
		int finalNumber;



		if (new_quantity >= max_quantity){
			finalNumber = max_addable_quantity;
		} else {
			finalNumber = number;
		}

		if (0 <= finalNumber) {
			if (quantity.isGreaterThanOrEqualTo(Quantity.of(0))) {
				getMaterialInventory().findById(materialId).ifPresent(itQM ->
					getMaterialInventory().save(itQM.increaseQuantity(Quantity.of(finalNumber, metric)))
				);
			}
		}

	}

	public void consume(InventoryItemIdentifier materialId, String sQuantity, int number){
		
		String newQ = "";

		for (int i = 0; i < sQuantity.length(); i++) {
			String test = "" + sQuantity.charAt(i);
			if(!Pattern.matches("\\d+", test)){
				break;
			}
			newQ += sQuantity.charAt(i);
		}



		int iQuantity = Integer.parseInt(newQ);
		Quantity quantity = Quantity.of(iQuantity);
		Metric metric = getMaterialInventory().findById(materialId).get().getQuantity().getMetric();

		int old_quantity = getMaterialInventory().findById(materialId).get().getQuantity().getAmount().toBigInteger().intValue();
		int new_quantity = old_quantity - number;

		if (new_quantity < 0) {
			number = old_quantity;
		}

		int finalNumber = number;

		if (quantity.isGreaterThanOrEqualTo(Quantity.of(0))) {
			getMaterialInventory().findById(materialId).ifPresent(itQM ->
				getMaterialInventory().save(itQM.decreaseQuantity(Quantity.of(finalNumber, metric)))
			);
		}

	}

}
