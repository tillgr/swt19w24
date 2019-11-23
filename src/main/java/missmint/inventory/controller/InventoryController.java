package missmint.inventory.controller;


import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
	@PreAuthorize("isAuthenticated()")
	String material(Model model) {
		model.addAttribute("material", Streamable.of(
			getMaterialInventory().findAll()).filter(it ->
			it.getProduct().getCategories().toList().get(0).equals("UNIT_MATERIAL"))
			.and(Streamable.of(getMaterialInventory().findAll()).filter(it ->
				it.getProduct().getCategories().toList().get(0).equals("LITER_MATERIAL")))
			.and(Streamable.of(getMaterialInventory().findAll()).filter(it ->
				it.getProduct().getCategories().toList().get(0).equals("METER_MATERIAL")))
			.and(Streamable.of(getMaterialInventory().findAll()).filter(it ->
				it.getProduct().getCategories().toList().get(0).equals("SQUARE_METER_MATERIAL")))
		);



		return "material";
	}

	@PostMapping("/material/consume/{quantity}/{materialId}")
	@PreAuthorize("isAuthenticated()")
	String consume(@PathVariable InventoryItemIdentifier materialId, @PathVariable Quantity quantity, @RequestParam("consumeNumber") int number){
		// TODO: 20.11.2019 fix for square meter - convert String into quantity?


		int old_quantity = getMaterialInventory().findById(materialId).get().getQuantity().getAmount().toBigInteger().intValue();
		int new_quantity = old_quantity - number;

		if (new_quantity < 0) {
			number = old_quantity;
		}

		int finalNumber = number;

			if ((quantity.getMetric().equals(Metric.SQUARE_METER)) && quantity.isGreaterThanOrEqualTo(Quantity.of(0, Metric.SQUARE_METER))) {
				getMaterialInventory().findById(materialId).ifPresent(itQM ->
					getMaterialInventory().save(itQM.decreaseQuantity(Quantity.of(finalNumber, Metric.SQUARE_METER)))
				);
			}

			if (quantity.getMetric().equals(Metric.UNIT) && quantity.isGreaterThanOrEqualTo(Quantity.of(0, Metric.UNIT))) {
				getMaterialInventory().findById(materialId).ifPresent(itQM ->
					getMaterialInventory().save(itQM.decreaseQuantity(Quantity.of(finalNumber, Metric.UNIT)))
				);
			}

			if (quantity.getMetric().equals(Metric.METER) && quantity.isGreaterThanOrEqualTo(Quantity.of(0, Metric.METER))) {
				getMaterialInventory().findById(materialId).ifPresent(itQM ->
					getMaterialInventory().save(itQM.decreaseQuantity(Quantity.of(finalNumber, Metric.METER)))
				);
			}

		if (quantity.getMetric().equals(Metric.LITER) && quantity.isGreaterThanOrEqualTo(Quantity.of(0, Metric.LITER))) {
			getMaterialInventory().findById(materialId).ifPresent(itQM ->
				getMaterialInventory().save(itQM.decreaseQuantity(Quantity.of(finalNumber, Metric.LITER)))
			);
		}

		return "redirect:/material";
	}

	@PostMapping("/material/restock/{quantity}/{materialId}")
	@PreAuthorize("isAuthenticated()")
	String restock(@PathVariable InventoryItemIdentifier materialId, @PathVariable Quantity quantity, @RequestParam("restockNumber") int number){
		// TODO: 20.11.2019 fix for square meter - convert String into quantity?

		int max_quantity = 900;
		int max_addable_quantity = max_quantity - (quantity.getAmount().intValueExact());
		int old_quantity = getMaterialInventory().findById(materialId).get().getQuantity().getAmount().intValueExact();
		int new_quantity = old_quantity + number;
		int finalNumber;


		if (new_quantity >= max_addable_quantity){
			 finalNumber = max_addable_quantity;
		} else {
			finalNumber = number;
		}

		if (0 <= finalNumber) {
			if (quantity.getMetric().equals(Metric.SQUARE_METER)) {
				getMaterialInventory().findById(materialId).ifPresent(itNQM ->
					getMaterialInventory().save(itNQM.increaseQuantity(Quantity.of(finalNumber, Metric.SQUARE_METER)))
				);
			}

			if (quantity.getMetric().equals(Metric.UNIT)) {
				getMaterialInventory().findById(materialId).ifPresent(itQM ->
					getMaterialInventory().save(itQM.increaseQuantity(Quantity.of(finalNumber, Metric.UNIT)))
				);
			}

			if (quantity.getMetric().equals(Metric.METER)) {
				getMaterialInventory().findById(materialId).ifPresent(itQM ->
					getMaterialInventory().save(itQM.increaseQuantity(Quantity.of(finalNumber, Metric.METER)))
				);
			}

			if (quantity.getMetric().equals(Metric.LITER)) {
				getMaterialInventory().findById(materialId).ifPresent(itQM ->
					getMaterialInventory().save(itQM.increaseQuantity(Quantity.of(finalNumber, Metric.LITER)))
				);
			}


		}
		return "redirect:/material";
	}
}
