package missmint.orders.service;

import com.mysema.commons.lang.Assert;
import javafx.util.Pair;
import missmint.inventory.products.Material;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceConsumption {

	private Map<MissMintService, Set<Pair<Material, Quantity>>> serviceMatRelation;

	public ServiceConsumption() {
		serviceMatRelation = new HashMap<>();
	}

	public Set<Pair<Material, Quantity>> getSetOfMaterialQuantity(MissMintService service) {
		return serviceMatRelation.get(service);
	}

	public boolean setServiceMaterialCost(MissMintService service, Collection<Pair<Material, Quantity>> matCostSet) {

		Assert.notNull(service, "Service must not be null.");
		Assert.notNull(matCostSet, "Collection must not be null.");

		var set = serviceMatRelation.computeIfAbsent(service, k -> new HashSet<>());
		return set.addAll(matCostSet);
	}
}
