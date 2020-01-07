package missmint.inventory.manager;

import missmint.finance.service.FinanceService;
import missmint.inventory.products.Material;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import javax.money.MonetaryAmount;

@Service
public class MaterialManager {


	private final UniqueInventory<UniqueInventoryItem> materialInventory;
	private final Catalog<Material> materialCatalog;
	private final FinanceService financeService;

	public MaterialManager(UniqueInventory<UniqueInventoryItem> materialInventory, Catalog<Material> materialCatalog, FinanceService financeService) {
		this.materialInventory = materialInventory;
		this.materialCatalog = materialCatalog;
		this.financeService = financeService;
	}

	public void billMaterial(UniqueInventoryItem material, int restockAmount) {
		MonetaryAmount price = material.getProduct().getPrice().multiply(restockAmount);
		financeService.add(String.format("material restock for %s", material.getProduct().getName()), price.negate());
	}

	public int checkStock(UniqueInventoryItem material) {
		int iQuantity = material.getQuantity().getAmount().intValueExact();
		int threshold = 20;
		int restockAmount;

		if (iQuantity < threshold) {
			restockAmount = threshold - iQuantity;
			return restock(material, restockAmount);
		}
		return 0;
	}

	public UniqueInventoryItem checkAndRestock(InventoryItemIdentifier materialId, int amount){
		return materialInventory.findById(materialId).map(material -> {
			Metric materialMetric = material.getQuantity().getMetric();
			int restockAmount =	restock(material, amount);

			billMaterial(material, restockAmount);
			materialInventory.save(material.increaseQuantity(Quantity.of(restockAmount, materialMetric)));
			return material;
		}).orElseThrow(() -> new IllegalArgumentException(materialId.toString()));
	}

	private int restock(UniqueInventoryItem material, int amount) {
		int max_quantity = 10000;
		int old_quantity = material.getQuantity().getAmount().intValueExact();
		int new_quantity = old_quantity + amount;
		int max_addable_quantity = max_quantity - (old_quantity);
		return new_quantity >= max_quantity ? max_addable_quantity : amount;
	}

	public UniqueInventoryItem checkAndConsume(InventoryItemIdentifier materialId, int amount){
		return materialInventory.findById(materialId).map(material -> {
			Metric materialMetric = material.getQuantity().getMetric();
			int consumeAmount;
			int restockAmount;

			consumeAmount = consume(material, amount);
			materialInventory.save(material.decreaseQuantity(Quantity.of(consumeAmount,materialMetric)));

			restockAmount = checkStock(material);
			materialInventory.save(material.increaseQuantity(Quantity.of(restockAmount,materialMetric)));

			billMaterial(material,restockAmount);
			return material;
		}).orElseThrow(()-> new IllegalArgumentException(materialId.toString()));
	}

	private int consume(UniqueInventoryItem material, int amount) {
		int old_quantity = material.getQuantity().getAmount().intValueExact();
		int new_quantity = old_quantity - amount;
		if (new_quantity < 0) {
			amount = old_quantity;
		}
		return amount;
	}

	public Streamable<UniqueInventoryItem> findMaterials() {
		return Streamable.of(materialInventory.findAll()).filter(it -> it.getProduct().getCategories().toList().get(0).endsWith("MATERIAL"));
	}

	public Material fromName(String name) {
		return materialCatalog.findByName(name).filter(material ->
				material.getCategories().filter(cat ->
						cat.endsWith("MATERIAL")
				).stream().findAny().isPresent()
		).stream().findAny().orElseThrow(() ->
				new RuntimeException("material not found")
		);

	}
}
