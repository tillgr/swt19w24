package missmint.orders.service;

import missmint.inventory.products.Material;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.quantity.Metric;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Set;

import static org.salespointframework.core.Currencies.EURO;

@Component
public class ServiceConsumptionInitializer implements DataInitializer {

	private ServiceConsumptionManager manager;

	public ServiceConsumptionInitializer(ServiceConsumptionManager manager) {
		Assert.notNull(manager, "ServiceConsumptionManager must not be null");
		this.manager = manager;
	}

	@Override
	public void initialize() {
		var set = Set.of(
			new Material("Schrauben", Money.of(10, EURO), Metric.UNIT),
			new Material("Muttern", Money.of(10, EURO), Metric.UNIT)
		);

		var electronicsConsumptionMap = new HashMap<ProductIdentifier, Integer>();
		set.forEach(material -> electronicsConsumptionMap.put(material.getId(), 5));
		manager.save(new ServiceConsumption(ServiceCategory.ELECTRONICS, electronicsConsumptionMap));

		var sewingConsumptionMap = new HashMap<ProductIdentifier, Integer>();

		set = Set.of(new Material("Stoff", Money.of(10, EURO), Metric.UNIT));

		set.forEach(material -> sewingConsumptionMap.put(material.getId(), 1));
		manager.save(new ServiceConsumption(ServiceCategory.SEWING, sewingConsumptionMap));

		var consumption = manager.findConsumptionByCategory(ServiceCategory.ELECTRONICS);
		consumption.ifPresent(serviceConsumption -> System.out.println(serviceConsumption.getServiceCategory().name()));
	}
}
