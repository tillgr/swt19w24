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

	private MaterialManager materialManager;

	public InventoryController(MaterialManager materialManager) {
		this.materialManager = materialManager;
	}

	@GetMapping("/material")
	@PreAuthorize("isAuthenticated()")
	public String material(Model model) {
		model.addAttribute("material", materialManager.findMaterials());
		return "material";
	}

	@PostMapping("/material/consume")
	@PreAuthorize("isAuthenticated()")
	public String consume(@RequestParam InventoryItemIdentifier material, @RequestParam("consumeNumber") int number) {
		materialManager.consume(material, number);

		return "redirect:/material";
	}

	@PostMapping("/material/restock")
	@PreAuthorize("isAuthenticated()")
	public String restock(@RequestParam InventoryItemIdentifier material, @RequestParam("restockNumber") int number) {
		materialManager.restock(material, number);

		return "redirect:/material";
	}
}

