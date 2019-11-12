package missmint.rooms;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class RoomController {

	private RoomsRepository rooms;
	private EntriesRepository entries;

	RoomController(RoomsRepository rooms, EntriesRepository entries){
		this.rooms = rooms;
		this.entries = entries;
	}

	@GetMapping("/rooms")
	//@PreAuthorize("isAuthenticated()")
	public String showRooms(Model model) {
		AddRoomForm form = new AddRoomForm("", "");

		model.addAttribute("form", form);
		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());

		return "rooms";
	}

	@PostMapping("/rooms/add")
	public String addRoom(Model model, @Valid @ModelAttribute("form") AddRoomForm form, Errors errors){
		if(errors.hasErrors()){
			return "rooms";
		}
		rooms.save(form.createRoom());

		return "redirect:/rooms";
	}

	@PostMapping("/rooms/{id}/delete")
	public String deleteRoom(@PathVariable("id") long id){
		rooms.findById(id).map(room -> {	//nimmt Wert falls nicht leer, dann wird funktion delete aufgerufen
			rooms.delete(room);
			return 1;
			}
		);

		return "redirect:/rooms";
	}

	public String listFreeSlots(){}

	public String bookFreeRoom(){}

	public String addEntry(){}

}
