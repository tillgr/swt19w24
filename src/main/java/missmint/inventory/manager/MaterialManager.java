package missmint.inventory.manager;

import missmint.inventory.products.Material;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class MaterialManager {

	private final UniqueInventory<UniqueInventoryItem> materialInventory;
	private final Catalog<Material> materialCatalog;


	public MaterialManager(UniqueInventory<UniqueInventoryItem> materialInventory, Catalog<Material> materialCatalog) {
		this.materialInventory = materialInventory;
		this.materialCatalog = materialCatalog;
	}

	public UniqueInventory<UniqueInventoryItem> getMaterialInventory() {
		return materialInventory;
	}

	private Quantity convertToQuantity(String sQuantity){
		StringBuilder newQuantity = new StringBuilder();

		for (int i = 0; i < sQuantity.length(); i++) {
			String test = "" + sQuantity.charAt(i);
			if(!Pattern.matches("\\d+", test)){
				break;
			}
			newQuantity.append(sQuantity.charAt(i));
		}

		String finalQuantity = newQuantity.toString();

		int iQuantity = Integer.parseInt(finalQuantity);

		//double dQuantity = Double.parseDouble(finalQuantity);

		return Quantity.of(iQuantity);
	}

	public void restock(String materialName, int number){

		Optional<UniqueInventoryItem> item1 = getMaterialInventory().findByProductIdentifier(Objects.requireNonNull(materialCatalog.findByName(materialName).toList().get(0).getId()));
		Quantity quantity = getMaterialInventory().findById(Objects.requireNonNull(item1.get().getId())).get().getQuantity();
		Metric metric = getMaterialInventory().findById(Objects.requireNonNull(item1.get().getId())).get().getQuantity().getMetric();

		int max_quantity = 900;
		int max_addable_quantity = max_quantity - (quantity.getAmount().intValueExact());
		int old_quantity = getMaterialInventory().findById(item1.get().getId()).get().getQuantity().getAmount().toBigInteger().intValue();
		int new_quantity = old_quantity + number;
		int finalNumber;



		if (new_quantity >= max_quantity){
			finalNumber = max_addable_quantity;
		} else {
			finalNumber = number;
		}

		if (0 <= finalNumber) {
			if (quantity.isGreaterThanOrEqualTo(Quantity.of(0))) {
				getMaterialInventory().findById(item1.get().getId()).ifPresent(itQM ->
					getMaterialInventory().save(itQM.increaseQuantity(Quantity.of(finalNumber, metric)))
				);
			}
		}

	}


	public void consume(String materialName, int number){

		Optional<UniqueInventoryItem> item1 = getMaterialInventory().findByProductIdentifier(Objects.requireNonNull(materialCatalog.findByName(materialName).toList().get(0).getId()));
		//Quantity quantity = convertToQuantity(sQuantity);
		Quantity quantity = getMaterialInventory().findById(Objects.requireNonNull(item1.get().getId())).get().getQuantity();
		Metric metric = getMaterialInventory().findById(Objects.requireNonNull(item1.get().getId())).get().getQuantity().getMetric();

		int old_quantity = getMaterialInventory().findById(item1.get().getId()).get().getQuantity().getAmount().toBigInteger().intValue();
		int new_quantity = old_quantity - number;

		if (new_quantity < 0) {
			number = old_quantity;
		}

		int finalNumber = number;

		if (quantity.isGreaterThanOrEqualTo(Quantity.of(0))) {
			getMaterialInventory().findById(item1.get().getId()).ifPresent(itQM ->
				getMaterialInventory().save(itQM.decreaseQuantity(Quantity.of(finalNumber, metric)))
			);
		}

	}



}
