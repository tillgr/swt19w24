package missmint.rooms;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RoomController {

	private RoomRepository rooms;
	private EntryRepository entries;

	RoomController(RoomRepository rooms, EntryRepository entries){
		this.rooms = rooms;
		this.entries = entries;
	}

	@GetMapping("/rooms")
	@PreAuthorize("isAuthenticated()")
	public String showRooms(Model model) {
		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());

		return "rooms";
	}


	@PostMapping("/rooms/{room}/delete")
	public String deleteRoom(@PathVariable("room") Room room){
		rooms.delete(room);

		return "redirect:/rooms";
	}


}
