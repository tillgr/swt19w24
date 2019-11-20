package missmint.inventory.initializer;

import static org.salespointframework.core.Currencies.EURO;
import missmint.inventory.products.Material;
import missmint.inventory.products.orderItem;
import java.util.stream.Stream;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.quantity.Metric;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
class CatalogDataInitializer implements DataInitializer {


	private final Catalog<Material> materialCatalog;
	private final Catalog<orderItem> orderItemCatalog;

	CatalogDataInitializer(Catalog<Material> materialCatalog, Catalog<orderItem> orderItemCatalog) {
		Assert.notNull(materialCatalog, "Catalog must not be null!");
		this.materialCatalog = materialCatalog;
		this.orderItemCatalog = orderItemCatalog;
	}

	@Override
	public void initialize() {

		Stream.of(
			Pair.of(new Material("Schrauben", Money.of(1,EURO),Metric.UNIT), "QUANTIFIABLE_MATERIAL"),
			Pair.of(new Material("Stoff", Money.of(0.02,EURO),Metric.SQUARE_METER), "NON_QUANTIFIABLE_MATERIAL"),
			Pair.of(new Material("Muttern", Money.of(0.01,EURO),Metric.UNIT), "QUANTIFIABLE_MATERIAL")
		).forEach(materialStringPair-> {
				materialStringPair.getFirst().addCategory(materialStringPair.getSecond());
				materialCatalog.save(materialStringPair.getFirst());
			});

		Stream.of(
			Pair.of(new orderItem("Schuhe", Money.of(0.1,EURO),Metric.UNIT),"ORDER_ITEM"),
			Pair.of(new orderItem("Hose", Money.of(0.1,EURO),Metric.UNIT), "ORDER_ITEM")
		).forEach(orderItemStringPair -> {
			orderItemStringPair.getFirst().addCategory(orderItemStringPair.getSecond());
			orderItemCatalog.save(orderItemStringPair.getFirst());
		});

	}
}