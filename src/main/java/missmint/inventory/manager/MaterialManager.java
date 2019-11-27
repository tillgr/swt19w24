package missmint.inventory.manager;

import missmint.inventory.products.Material;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

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


	public void checkQuantity(UniqueInventoryItem item){

		Quantity quantity = item.getQuantity();
		Quantity threshold = Quantity.of(20);

		if (quantity.isLessThan(threshold)){
			restock(item.getId(), 10);
		}
	}


	public void restock(InventoryItemIdentifier material, int number){

		Optional<UniqueInventoryItem> item1 = getMaterialInventory().findById(material);
		Quantity quantity;
		Metric metric;


		int max_quantity = 900;
		int old_quantity;
		int new_quantity;
		int finalNumber;

		if (item1.isPresent()) {
			if (getMaterialInventory().findById(Objects.requireNonNull(item1.get().getId())).isPresent()) {

				quantity = getMaterialInventory().findById(Objects.requireNonNull(item1.get().getId())).get().getQuantity();
				metric = getMaterialInventory().findById(Objects.requireNonNull(item1.get().getId())).get().getQuantity().getMetric();
				old_quantity = getMaterialInventory().findById(Objects.requireNonNull(item1.get().getId())).get().getQuantity().getAmount().toBigInteger().intValue();

				int max_addable_quantity = max_quantity - (quantity.getAmount().intValueExact());
				new_quantity = old_quantity + number;


				if (new_quantity >= max_quantity) {
					finalNumber = max_addable_quantity;
				} else {
					finalNumber = number;
				}

				getMaterialInventory().findById(item1.get().getId()).ifPresent(itQM ->
					getMaterialInventory().save(itQM.increaseQuantity(Quantity.of(finalNumber, metric)))
				);
			}
		}
	}


	public void consume(InventoryItemIdentifier material, int number){

		Optional<UniqueInventoryItem> item1 = getMaterialInventory().findById(material);
		Metric metric;


		int old_quantity;
		int new_quantity;
		int finalNumber;

		if (item1.isPresent()) {
			if (getMaterialInventory().findById(Objects.requireNonNull(item1.get().getId())).isPresent()) {
				;
				metric = getMaterialInventory().findById(Objects.requireNonNull(item1.get().getId())).get().getQuantity().getMetric();
				old_quantity = getMaterialInventory().findById(Objects.requireNonNull(item1.get().getId())).get().getQuantity().getAmount().toBigInteger().intValue();
				new_quantity = old_quantity - number;


				if (new_quantity < 0) {
					number = old_quantity;
				}

				finalNumber = number;

				getMaterialInventory().findById(item1.get().getId()).ifPresent(itQM ->
					getMaterialInventory().save(itQM.decreaseQuantity(Quantity.of(finalNumber, metric)))
				);
			}
		}
	}
}
