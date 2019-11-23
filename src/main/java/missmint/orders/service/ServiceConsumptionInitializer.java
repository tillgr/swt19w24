package missmint.orders.service;

import com.mysema.commons.lang.Assert;
import javafx.util.Pair;
import missmint.inventory.products.Material;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ServiceConsumptionInitializer implements DataInitializer {

	private Catalog<Material> materialCatalog;
	private Catalog<MissMintService> serviceCatalog;
	private ServiceConsumption serviceConsumption;

	public ServiceConsumptionInitializer(Catalog<Material> materialCatalog, Catalog<MissMintService> serviceCatalog, ServiceConsumption serviceConsumption) {
		Assert.notNull(materialCatalog, "Catalog must not be null");
		Assert.notNull(serviceConsumption, "ServiceConsumption must not be null");
		Assert.notNull(serviceCatalog, "ServiceCatalog must not be null");
		this.materialCatalog = materialCatalog;
		this.serviceConsumption = serviceConsumption;
		this.serviceCatalog = serviceCatalog;
	}

	@Override
	public void initialize() {
		materialCatalog.findByCategory("QUANTIFIABLE_MATERIAL").forEach(material -> {
			switch (material.getName()) {
				case "Schrauben":
					serviceCatalog.findByAllCategories("SERVICE", ServiceCategory.ELECTRONICS.name()).forEach(service -> {
						System.out.println("Mapping " + material.getName() + " to " + service.getName());
						serviceConsumption.setServiceMaterialCost(
							service, Set.of(new Pair<>(material, Quantity.of(5, Metric.UNIT)))
						);
						}
					);
					break;
			}
		});
	}
}
