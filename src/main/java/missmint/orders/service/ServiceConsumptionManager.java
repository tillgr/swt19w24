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

	public final static Map<ServiceCategory, Set<Pair<Material, Quantity>>> serviceMatRelation = Map.of(
		ServiceCategory.KLUDGE,
		Set.of(
			new Pair<>(new Material("Stoff", Money.of(1, EURO), Metric.UNIT), Quantity.of(2, Metric.UNIT))
		),
		ServiceCategory.SEWING,
		Set.of(
			new Pair<>(new Material("Stoff", Money.of(1, EURO), Metric.UNIT), Quantity.of(2, Metric.UNIT)),
			new Pair<>(new Material("Naht", Money.of(1, EURO), Metric.METER), Quantity.of(2, Metric.METER))
		),
		ServiceCategory.LOCKSMITH,
		Set.of(),
		ServiceCategory.CLEANING,
		Set.of(
			new Pair<>(new Material("Reinigungsmittel", Money.of(1, EURO), Metric.LITER), Quantity.of(1, Metric.LITER))
		),
		ServiceCategory.ELECTRONICS,
		Set.of(
			new Pair<>(new Material("Kabel", Money.of(1, EURO), Metric.METER), Quantity.of(5, Metric.METER))
		),
		ServiceCategory.GRINDERY,
		Set.of()
	);
}
