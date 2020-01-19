package missmint.inventory.initializer;

import missmint.inventory.products.Material;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Adds a certain amount of every material in the catalog to the inventory.
 */
@Component
class InventoryInitializer implements DataInitializer {
	private final UniqueInventory<UniqueInventoryItem> materialInventory;
	private final Catalog<Material> materialCatalog;

	InventoryInitializer(Catalog<Material> materialCatalog, UniqueInventory<UniqueInventoryItem> materialInventory) {
		Assert.notNull(materialInventory, "materialInventory must not be null!");
		Assert.notNull(materialCatalog, "Catalog must not be null!");
		this.materialInventory = materialInventory;
		this.materialCatalog = materialCatalog;
	}

	@Override
	public void initialize() {
		materialCatalog.findByCategory("UNIT_MATERIAL").forEach(material -> {
			if (materialInventory.findByProduct(material).isEmpty()) {
				materialInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.UNIT)));
			}
		});

		materialCatalog.findByCategory("SQUARE_METER_MATERIAL").forEach(material -> {
			if (materialInventory.findByProduct(material).isEmpty()) {
				materialInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.SQUARE_METER)));
			}
		});

		materialCatalog.findByCategory("METER_MATERIAL").forEach(material -> {
			if (materialInventory.findByProduct(material).isEmpty()) {
				materialInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.METER)));
			}
		});

		materialCatalog.findByCategory("LITER_MATERIAL").forEach(material -> {
			if (materialInventory.findByProduct(material).isEmpty()) {
				materialInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.LITER)));
			}
		});
	}
}
