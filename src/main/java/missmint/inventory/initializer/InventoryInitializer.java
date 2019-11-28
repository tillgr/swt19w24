package missmint.inventory.initializer;

import missmint.inventory.products.Material;
import missmint.inventory.products.OrderItem;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
class InventoryInitializer implements DataInitializer {

	private final UniqueInventory<UniqueInventoryItem> materialInventory;
	private final UniqueInventory<UniqueInventoryItem> orderItemInventory;
	private final Catalog<Material> materialCatalog ;
	private final Catalog<OrderItem> orderItemCatalog ;

	InventoryInitializer(Catalog<Material> materialCatalog, Catalog<OrderItem> orderItemCatalog, UniqueInventory<UniqueInventoryItem> orderItemInventory, UniqueInventory<UniqueInventoryItem> materialInventory) {
		Assert.notNull(materialInventory, "materialInventory must not be null!");
		Assert.notNull(orderItemInventory, "orderItemInventory must not be null");
		Assert.notNull(materialCatalog, "Catalog must not be null!");
		Assert.notNull(orderItemCatalog,"Catalog must not be null");

		this.materialInventory = materialInventory;
		this.materialCatalog = materialCatalog;
		this.orderItemInventory = orderItemInventory;
		this.orderItemCatalog = orderItemCatalog;
	}


	@Override
	public void initialize() {
		materialCatalog.findByCategory("UNIT_MATERIAL").forEach(material -> {
			materialInventory.findByProduct(material)
				.orElseGet(() -> materialInventory.save(new UniqueInventoryItem(material,Quantity.of(100,Metric.UNIT))));
		});

		materialCatalog.findByCategory("SQUARE_METER_MATERIAL").forEach(material -> {
			materialInventory.findByProduct(material)
				.orElseGet(() -> materialInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.SQUARE_METER))));
		});

		materialCatalog.findByCategory("METER_MATERIAL").forEach(material -> {
			materialInventory.findByProduct(material)
				.orElseGet(() -> materialInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.METER))));
		});

		materialCatalog.findByCategory("LITER_MATERIAL").forEach(material -> {
			materialInventory.findByProduct(material)
				.orElseGet(() -> materialInventory.save(new UniqueInventoryItem(material, Quantity.of(100, Metric.LITER))));
		});

		orderItemCatalog.findByCategory("ORDER_ITEM").forEach(orderItem -> {
			orderItemInventory.findByProduct(orderItem)
				.orElseGet(() -> orderItemInventory.save(new UniqueInventoryItem(orderItem, Quantity.of(1,Metric.UNIT))));
		});
	}
}
