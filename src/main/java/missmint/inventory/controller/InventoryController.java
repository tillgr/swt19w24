package missmint.inventory.controller;

import missmint.inventory.MaterialCatalog;

import org.salespointframework.inventory.*;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;
import java.util.stream.Stream;

@Controller
public class InventoryController {

	private static final Quantity NONE = Quantity.of(0);

	private final MaterialCatalog catalog;
	private final UniqueInventory<UniqueInventoryItem> materialInventory;
	private final UniqueInventory<UniqueInventoryItem> orderItemInventory;

	InventoryController(MaterialCatalog materialCatalog, UniqueInventory<UniqueInventoryItem> materialInventory, UniqueInventory<UniqueInventoryItem> orderItemInventory) {
		this.materialInventory = materialInventory;
		this.orderItemInventory = orderItemInventory;
		this.catalog = materialCatalog;
	}


	public  UniqueInventory<UniqueInventoryItem> getMaterialInventory() {
		return materialInventory;
	}
	public  UniqueInventory<UniqueInventoryItem> getOrderItemInventory(){
		return orderItemInventory;
	}

	@GetMapping("/material")
	String material(Model model) {

		model.addAttribute("material", materialInventory.findAll());

		return "material";
	}


	@PostMapping("/material/consume/{quantity}/{materialId}")
	String consume(Model model, @PathVariable InventoryItemIdentifier materialId, @PathVariable Quantity quantity){
		// TODO: 20.11.2019 fix for square meter - convert String into quantity?

		if ((quantity.getMetric().equals(Metric.SQUARE_METER)) && quantity.isGreaterThanOrEqualTo(Quantity.of(10,Metric.SQUARE_METER))) {
			getMaterialInventory().findById(materialId).ifPresent(itQM ->
				getMaterialInventory().save(itQM.decreaseQuantity(Quantity.of(5,Metric.SQUARE_METER))));
		}

		if (quantity.getMetric().equals(Metric.UNIT) && quantity.isGreaterThanOrEqualTo(Quantity.of(5,Metric.UNIT))) {
			getMaterialInventory().findById(materialId).ifPresent(itQM ->
					getMaterialInventory().save(itQM.decreaseQuantity(Quantity.of(5,Metric.UNIT)))
			);
		}
		return "redirect:/material";
	}

	@PostMapping("/material/restock/{quantity}/{materialId}")
	String restock(Model model, @PathVariable InventoryItemIdentifier materialId, @PathVariable Quantity quantity){
		// TODO: 20.11.2019 fix for square meter - convert String into quantity?

		if (quantity.getMetric().equals(Metric.SQUARE_METER)) {
			getMaterialInventory().findById(materialId).ifPresent(itNQM ->
					getMaterialInventory().save(itNQM.increaseQuantity(Quantity.of(5, Metric.SQUARE_METER)))
			);
		}

		if (quantity.getMetric().equals(Metric.UNIT)) {
			getMaterialInventory().findById(materialId).ifPresent(itQM ->
					getMaterialInventory().save(itQM.increaseQuantity(Quantity.of(5,Metric.UNIT)))
			);
		}
		return "redirect:/material";
	}
}
