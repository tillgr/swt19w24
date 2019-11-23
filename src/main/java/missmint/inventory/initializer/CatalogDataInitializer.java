package missmint.inventory.initializer;

import static org.salespointframework.core.Currencies.EURO;
import missmint.inventory.products.Material;
import missmint.inventory.products.OrderItem;

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
	private final Catalog<OrderItem> orderItemCatalog;

	CatalogDataInitializer(Catalog<Material> materialCatalog, Catalog<OrderItem> orderItemCatalog) {
		Assert.notNull(materialCatalog, "Catalog must not be null!");
		this.materialCatalog = materialCatalog;
		this.orderItemCatalog = orderItemCatalog;
	}

	@Override
	public void initialize() {

		Stream.of(
			Pair.of(new Material("Absatz", Money.of(0.5, EURO), Metric.UNIT), "UNIT_MATERIAL"),
			Pair.of(new Material("Sohle", Money.of(0.5, EURO), Metric.UNIT), "UNIT_MATERIAL"),
			Pair.of(new Material("Faden", Money.of(0.02,EURO), Metric.METER), "METER_MATERIAL"),

			Pair.of(new Material("Faden", Money.of(0.01,EURO), Metric.UNIT), "UNIT_MATERIAL"),
			Pair.of(new Material("Stoff", Money.of(0.02,EURO), Metric.SQUARE_METER), "SQUARE_METER_MATERIAL"),

			Pair.of(new Material("Schlüsselrohling", Money.of(1,EURO), Metric.UNIT), "UNIT_MATERIAL"),

			Pair.of(new Material("Waschmittel", Money.of(1,EURO), Metric.LITER), "LITER_MATERIAL"),

			Pair.of(new Material("Lötdraht", Money.of(1,EURO), Metric.METER), "METER_MATERIAL"),
			Pair.of(new Material("Flussmittel", Money.of(1,EURO), Metric.LITER), "LITER_MATERIAL"),
			Pair.of(new Material("Schrauben", Money.of(1,EURO), Metric.UNIT), "UNIT_MATERIAL"),
			Pair.of(new Material("Muttern", Money.of(0.01,EURO), Metric.UNIT), "UNIT_MATERIAL"),

			Pair.of(new Material("Schleifpapier", Money.of(1,EURO), Metric.UNIT), "UNIT_MATERIAL")
		).forEach(materialStringPair-> {
				materialStringPair.getFirst().addCategory(materialStringPair.getSecond());
				materialCatalog.save(materialStringPair.getFirst());
			});

		Stream.of(
			Pair.of(new OrderItem("Schuhe", Money.of(0.0,EURO),Metric.UNIT),"ORDER_ITEM"),
			Pair.of(new OrderItem("Hose", Money.of(0.0,EURO),Metric.UNIT), "ORDER_ITEM")
		).forEach(orderItemStringPair -> {
			orderItemStringPair.getFirst().addCategory(orderItemStringPair.getSecond());
			orderItemCatalog.save(orderItemStringPair.getFirst());
		});

	}
}