package missmint.inventory.controller;

import org.salespointframework.inventory.*;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class InventoryController {

	private final UniqueInventory<UniqueInventoryItem> materialInventory;

	InventoryController(UniqueInventory<UniqueInventoryItem> materialInventory) {
		this.materialInventory = materialInventory;
	}

	private UniqueInventory<UniqueInventoryItem> getMaterialInventory() {
		return materialInventory;
	}

	@GetMapping("/material")
	String material(Model model) {
		model.addAttribute("material", materialInventory.findAll());
		return "material";
	}

	@PostMapping("/material/consume/{quantity}/{materialId}")
	String consume(@PathVariable InventoryItemIdentifier materialId, @PathVariable Quantity quantity){
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
	String restock(@PathVariable InventoryItemIdentifier materialId, @PathVariable Quantity quantity){
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
