package missmint.inventory.controller;


import missmint.Utils;
import missmint.inventory.manager.MaterialManager;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class InventoryController {

	private MaterialManager materialManager;
	private final UniqueInventory<UniqueInventoryItem> materialInventory;

	public InventoryController(MaterialManager materialManager, UniqueInventory<UniqueInventoryItem> materialInventory) {
		this.materialManager = materialManager;
		this.materialInventory = materialInventory;
	}

	@GetMapping("/material")
	@PreAuthorize("isAuthenticated()")
	public String material(Model model) {
		model.addAttribute("material", materialManager.findMaterials());
		return "material";
	}

	@PostMapping("/material/consume")
	@PreAuthorize("isAuthenticated()")
	public String consume(@RequestParam InventoryItemIdentifier material, @RequestParam("number") int number) {
		materialManager.consume(material, number);
		materialManager.autoRestock(Utils.getOrThrow(materialInventory.findById(material)));

		return "redirect:/material";
	}

	@PostMapping("/material/restock")
	@PreAuthorize("isAuthenticated()")
	public String restock(@RequestParam InventoryItemIdentifier material, @RequestParam("number") int number) {
		int restockedAmount = materialManager.restock(material, number);
		materialManager.restockAccountancy(Utils.getOrThrow(materialInventory.findById(material)), restockedAmount);

		return "redirect:/material";
	}
}

