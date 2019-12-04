package missmint.inventory.manager;

import missmint.finance.FinanceService;
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
import java.util.Objects;
import java.util.Optional;

@Service
public class MaterialManager {

	private static final Quantity THRESHOLD = Quantity.of(20);

	private final UniqueInventory<UniqueInventoryItem> materialInventory;
	private final Catalog<Material> materialCatalog;
	private final FinanceService financeService;

	public MaterialManager(UniqueInventory<UniqueInventoryItem> materialInventory, Catalog<Material> materialCatalog, FinanceService financeService) {
		this.materialInventory = materialInventory;
		this.materialCatalog = materialCatalog;
		this.financeService = financeService;
	}

	public void autoRestock(UniqueInventoryItem item) {
		Quantity quantity = item.getQuantity();

		if (quantity.isLessThan(THRESHOLD)) {
			Quantity RESTOCK_AMOUNT = THRESHOLD.subtract(quantity);
			restock(item.getId(), RESTOCK_AMOUNT.getAmount().intValueExact());

			restockAccountancy(item, RESTOCK_AMOUNT.getAmount().intValueExact());
		}
	}

	public void restockAccountancy(UniqueInventoryItem item, int restockAmount) {
		MonetaryAmount price = item.getProduct().getPrice().multiply(restockAmount);
		financeService.add(String.format("material restock for %s", item.getProduct().getName()), price.negate());
	}

	public int restock(InventoryItemIdentifier material, int number) {
		Optional<UniqueInventoryItem> item = materialInventory.findById(material);
		Quantity quantity;
		Metric metric;

		int max_quantity = 10000;
		int old_quantity;
		int new_quantity;
		int finalNumber;

		if (item.isPresent()) {
			if (materialInventory.findById(Objects.requireNonNull(item.get().getId())).isPresent()) {
				quantity = materialInventory.findById(Objects.requireNonNull(item.get().getId())).get().getQuantity();
				metric = materialInventory.findById(Objects.requireNonNull(item.get().getId())).get().getQuantity().getMetric();
				old_quantity = materialInventory.findById(Objects.requireNonNull(item.get().getId())).get().getQuantity().getAmount().toBigInteger().intValueExact();
				int max_addable_quantity = max_quantity - (quantity.getAmount().intValueExact());
				new_quantity = old_quantity + number;

				if (new_quantity >= max_quantity) {
					finalNumber = max_addable_quantity;
				} else {
					finalNumber = number;
				}

				materialInventory.findById(item.get().getId()).ifPresent(itQM ->
					materialInventory.save(itQM.increaseQuantity(Quantity.of(finalNumber, metric)))
				);
				return finalNumber;
			}
		}
		return 0;
	}

	public void consume(InventoryItemIdentifier material, int number) {
		Optional<UniqueInventoryItem> item = materialInventory.findById(material);
		Metric metric;

		int old_quantity;
		int new_quantity;
		int finalNumber;

		if (item.isPresent()) {
			if (materialInventory.findById(Objects.requireNonNull(item.get().getId())).isPresent()) {
				metric = materialInventory.findById(Objects.requireNonNull(item.get().getId())).get().getQuantity().getMetric();
				old_quantity = materialInventory.findById(Objects.requireNonNull(item.get().getId())).get().getQuantity().getAmount().toBigInteger().intValue();
				new_quantity = old_quantity - number;

				if (new_quantity < 0) {
					number = old_quantity;
				}

				finalNumber = number;

				materialInventory.findById(item.get().getId()).ifPresent(itQM ->
					materialInventory.save(itQM.decreaseQuantity(Quantity.of(finalNumber, metric)))
				);
			}
		}
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
