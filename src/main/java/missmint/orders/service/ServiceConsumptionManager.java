package missmint.orders.service;

import javafx.util.Pair;
import missmint.inventory.products.Material;
import org.javamoney.moneta.Money;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

import java.util.Map;
import java.util.Set;

import static org.salespointframework.core.Currencies.EURO;

public class ServiceConsumptionManager {

	public final static Map<ServiceCategory, Set<Pair<String, Quantity>>> serviceMatRelation = Map.of(
		ServiceCategory.KLUDGE,
		Set.of(
			new Pair<>("heel", Quantity.of(2, Metric.UNIT)),
			new Pair<>("sole", Quantity.of(2, Metric.UNIT))
		),
		ServiceCategory.SEWING,
		Set.of(
			new Pair<>("string", Quantity.of(1, Metric.METER)),
			new Pair<>("fabric", Quantity.of(1, Metric.SQUARE_METER))
		),
		ServiceCategory.LOCKSMITH,
		Set.of(new Pair<>("blank key", Quantity.of(1, Metric.UNIT))),
		ServiceCategory.CLEANING,
		Set.of(new Pair<>("detergent", Quantity.of(10, Metric.LITER))),
		ServiceCategory.ELECTRONICS,
		Set.of(
			new Pair<>("soldering wire", Quantity.of(2, Metric.METER)),
			new Pair<>("soldering flux", Quantity.of(1, Metric.LITER)),
			new Pair<>("screws", Quantity.of(1, Metric.UNIT)),
			new Pair<>("nuts", Quantity.of(1, Metric.UNIT))
		),
		ServiceCategory.GRINDERY,
		Set.of(new Pair<>("sanding-paper", Quantity.of(2, Metric.UNIT)))
	);
}
