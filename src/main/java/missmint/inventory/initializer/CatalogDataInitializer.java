package missmint.inventory.initializer;

import missmint.inventory.products.Material;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.quantity.Metric;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.stream.Stream;

import static org.salespointframework.core.Currencies.EURO;

/**
 * Creates catalog entries of all materials needed for the repair shop.
 */
@Component
class CatalogDataInitializer implements DataInitializer {

	private final Catalog<Material> materialCatalog;

	CatalogDataInitializer(Catalog<Material> materialCatalog) {
		Assert.notNull(materialCatalog, "Catalog must not be null!");
		this.materialCatalog = materialCatalog;
	}

	@Override
	public void initialize() {
		if (!materialCatalog.findByCategory("UNIT_MATERIAL").isEmpty()) {
			return;
		}
		if (!materialCatalog.findByCategory("LITER_MATERIAL").isEmpty()) {
			return;
		}
		if (!materialCatalog.findByCategory("METER_MATERIAL").isEmpty()) {
			return;
		}
		if (!materialCatalog.findByCategory("SQUARE_METER_MATERIAL").isEmpty()) {
			return;
		}

		Stream.of(
				Pair.of(new Material("heel", Money.of(0.5, EURO), Metric.UNIT), "UNIT_MATERIAL"),
				Pair.of(new Material("sole", Money.of(0.5, EURO), Metric.UNIT), "UNIT_MATERIAL"),
				Pair.of(new Material("string", Money.of(0.3, EURO), Metric.METER), "METER_MATERIAL"),
				Pair.of(new Material("fabric", Money.of(2, EURO), Metric.SQUARE_METER), "SQUARE_METER_MATERIAL"),
				Pair.of(new Material("blank key", Money.of(1.2, EURO), Metric.UNIT), "UNIT_MATERIAL"),
				Pair.of(new Material("detergent", Money.of(1.5, EURO), Metric.LITER), "LITER_MATERIAL"),
				Pair.of(new Material("soldering wire", Money.of(0.4, EURO), Metric.METER), "METER_MATERIAL"),
				Pair.of(new Material("soldering flux", Money.of(0.2, EURO), Metric.LITER), "LITER_MATERIAL"),
				Pair.of(new Material("screw", Money.of(0.015, EURO), Metric.UNIT), "UNIT_MATERIAL"),
				Pair.of(new Material("nut", Money.of(0.7, EURO), Metric.UNIT), "UNIT_MATERIAL"),
				Pair.of(new Material("sanding paper", Money.of(0.12, EURO), Metric.UNIT), "UNIT_MATERIAL")
		).forEach(materialStringPair -> {
			materialStringPair.getFirst().addCategory(materialStringPair.getSecond());
			materialCatalog.save(materialStringPair.getFirst());
		});
	}
}
