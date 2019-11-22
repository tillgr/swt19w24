package missmint.inventory.controller;


import missmint.inventory.products.Material;
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

import java.util.Objects;


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

	@PostMapping("/material/consume/{sQuantity}/{materialId}")
	@PreAuthorize("isAuthenticated()")
	String consume(@PathVariable InventoryItemIdentifier materialId, @PathVariable String sQuantity, @RequestParam("consumeNumber") int number){
		// TODO: 20.11.2019 fix for square meter - convert String into quantity?

		String newQ = "";
		for (int i = 0; i < sQuantity.length(); i++) {

			if (sQuantity.charAt(i) == 'm' || sQuantity.charAt(i) == 'l') {
				break;
			}
			newQ += sQuantity.charAt(i);
		}

		int iQuantity = Integer.parseInt(newQ);
		Quantity quantity = Quantity.of(iQuantity);
		Metric metric = getMaterialInventory().findById(materialId).get().getQuantity().getMetric();

		int old_quantity = getMaterialInventory().findById(materialId).get().getQuantity().getAmount().toBigInteger().intValue();
		int new_quantity = old_quantity - number;

		if (new_quantity < 0) {
			number = old_quantity;
		}

		int finalNumber = number;

		if (quantity.isGreaterThanOrEqualTo(Quantity.of(0))) {
			getMaterialInventory().findById(materialId).ifPresent(itQM ->
				getMaterialInventory().save(itQM.decreaseQuantity(Quantity.of(finalNumber, metric)))
			);
		}

		return "redirect:/material";
	}

	@PostMapping("/material/restock/{sQuantity}/{materialId}")
	@PreAuthorize("isAuthenticated()")
	String restock(@PathVariable InventoryItemIdentifier materialId, @PathVariable String sQuantity, @RequestParam("restockNumber") int number){
		// TODO: 20.11.2019 fix for square meter - convert String into quantity?

		String newQ = "";
		for (int i = 0; i < sQuantity.length(); i++) {

			if (sQuantity.charAt(i) == 'm' || sQuantity.charAt(i) == 'l') {
				break;
			}
			newQ += sQuantity.charAt(i);
		}

		int iQuantity = Integer.parseInt(newQ);
		Quantity quantity = Quantity.of(iQuantity);
		Metric metric = getMaterialInventory().findById(materialId).get().getQuantity().getMetric();

		int max_quantity = 900;
		int max_addable_quantity = max_quantity - (quantity.getAmount().intValueExact());
		int old_quantity = getMaterialInventory().findById(materialId).get().getQuantity().getAmount().intValueExact();
		int new_quantity = old_quantity + number;
		int finalNumber;


		if (new_quantity >= max_quantity){
			finalNumber = max_addable_quantity;
		} else {
			finalNumber = number;
		}

		if (0 <= finalNumber) {
			if (quantity.isGreaterThanOrEqualTo(Quantity.of(0))) {
				getMaterialInventory().findById(materialId).ifPresent(itQM ->
					getMaterialInventory().save(itQM.increaseQuantity(Quantity.of(finalNumber, metric)))
				);
			}
		}

		return "redirect:/material";
	}

	public void consume(UniqueInventoryItem item1, Material material1){
		var id1 = getMaterialInventory().findByProductIdentifier(Objects.requireNonNull(material1.getId())).stream().iterator().next().getId();
		if (item1.getQuantity().getMetric().equals(Metric.UNIT)){
			getMaterialInventory().findById(id1).ifPresent(itNQM ->
				getMaterialInventory().save(itNQM.increaseQuantity(Quantity.of(5, Metric.SQUARE_METER)))
			);
		}
	}
}

