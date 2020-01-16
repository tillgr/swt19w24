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
	int max_quantity = 10000;

	public MaterialManager(UniqueInventory<UniqueInventoryItem> materialInventory, Catalog<Material> materialCatalog, FinanceService financeService) {
		this.materialInventory = materialInventory;
		this.materialCatalog = materialCatalog;
		this.financeService = financeService;
	}

	/**
	 * Adds a expense for restocking material.
	 *
	 * @param material Material which is to be billed.
	 * @param restockAmount Amount of material.
	 */
	public void billMaterial(UniqueInventoryItem material, int restockAmount) {
		MonetaryAmount price = material.getProduct().getPrice().multiply(restockAmount);
		financeService.add(String.format("material restock for %s", material.getProduct().getName()), price.negate());
	}

	/**
	 * Checks if an material needs to be restocked.
	 *
	 * @param material Material that gets checked.
	 * @return The amount that gets restocked.
	 */
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

	/**
	 * Restocks the material if it exists in the inventory.
	 *
	 * @param materialId Identifier of the material that gets restocked.
	 * @param amount Amount by which the material gets restocked.
	 * @return The material if it exists in the inventory.
	 * @throws IllegalArgumentException if the InventoryItemIdentifier supplied by the input does not exist in the inventory
	 */
	public UniqueInventoryItem checkAndRestock(InventoryItemIdentifier materialId, int amount){
		return materialInventory.findById(materialId).map(material -> {
			Metric materialMetric = material.getQuantity().getMetric();
			int restockAmount =	restock(material, amount);

			billMaterial(material, restockAmount);
			materialInventory.save(material.increaseQuantity(Quantity.of(restockAmount, materialMetric)));
			return material;
		}).orElseThrow(() -> new IllegalArgumentException(materialId.toString()));
	}

	/**
	 * @param material Material.
	 * @param amount Amount by which the material stock gets increased.
	 * @return The amount by which the quantity is increased.
	 */
	private int restock(UniqueInventoryItem material, int amount) {

		int old_quantity = material.getQuantity().getAmount().intValueExact();
		int new_quantity = old_quantity + amount;
		int max_addable_quantity = max_quantity - (old_quantity);
		return new_quantity > max_quantity ? 0 : amount;
	}

	/**
	 * @param materialId Identifier of the material that gets consumed
	 * @param amount Amount by which the material gets consumed
	 * @return The material if it exists in the inventory.
	 * @throws IllegalArgumentException if the InventoryItemIdentifier supplied by the input does not exist in the inventory
	 */
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
	/**
	 * @param material Material.
	 * @param amount Amount by which the material stock gets increased.
	 * @return The amount by which the quantity is increased.
	 */

	private int consume(UniqueInventoryItem material, int amount) {
		int old_quantity = material.getQuantity().getAmount().intValueExact();
		int new_quantity = old_quantity - amount;
		if (new_quantity < 0) {
			return 0;
		}
		return amount;
	}

	public Streamable<UniqueInventoryItem> findMaterials() {
		return Streamable.of(materialInventory.findAll()).filter(it ->
				it.getProduct().getCategories().toList().get(0).endsWith("MATERIAL"));
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
