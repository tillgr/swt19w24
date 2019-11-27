package missmint.inventory.controller;



import missmint.inventory.manager.MaterialManager;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;


@Controller
public class InventoryController {

	@Autowired
	MaterialManager materialManager ;



	@GetMapping("/material")
	@PreAuthorize("isAuthenticated()")
	String material(Model model) {
		model.addAttribute("material", Streamable.of(
			materialManager.getMaterialInventory().findAll()).filter(it ->
			it.getProduct().getCategories().toList().get(0).equals("UNIT_MATERIAL"))
			.and(Streamable.of(materialManager.getMaterialInventory().findAll()).filter(it ->
				it.getProduct().getCategories().toList().get(0).equals("LITER_MATERIAL")))
			.and(Streamable.of(materialManager.getMaterialInventory().findAll()).filter(it ->
				it.getProduct().getCategories().toList().get(0).equals("METER_MATERIAL")))
			.and(Streamable.of(materialManager.getMaterialInventory().findAll()).filter(it ->
				it.getProduct().getCategories().toList().get(0).equals("SQUARE_METER_MATERIAL")))
		);
		return "material";
	}



	@PostMapping("/material/consume")
	@PreAuthorize("isAuthenticated()")
	String consume(@RequestParam InventoryItemIdentifier material, @RequestParam("consumeNumber") int number){

		materialManager.consume(material, number);

		return "redirect:/material";
	}

	@PostMapping("/material/restock")
	@PreAuthorize("isAuthenticated()")
	String restock(@RequestParam InventoryItemIdentifier material, @RequestParam("restockNumber") int number){

		materialManager.restock(material, number);


		return "redirect:/material";
	}

	@PostMapping("material/test")
	String testString(){

		return "redirect:/material";
	}
}

