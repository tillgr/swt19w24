package missmint.rooms;

import missmint.time.EntryRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
		AddRoomForm form = new AddRoomForm("");

		model.addAttribute("form", form);
		model.addAttribute("rooms", rooms.findAll());

		return "rooms";
	}

	@PostMapping("/rooms/add")
	public String addRoom(Model model, @Valid @ModelAttribute("form") AddRoomForm form, Errors errors){
		if(errors.hasErrors()){
			return "rooms";
		}
		rooms.save(form.createRoom(entries, rooms));

		/*
		Iterator<TimeTableEntry> it = entries.findAll().iterator();
		for (int i = 0; i < 7; i++){
			TimeTableEntry currentEntry = it.next();
			if(currentEntry.getRoom() == null){		//&& rooms.findById(id).map(room -> {return currentEntry.getRoom().equals(room); }).orElse(false)
				rooms.findById(form.createRoom().getId()).map(room -> {	//TODO beim erstellen eines raumes sollen die nächsten 6 entries den raum zugewiesen bekommen
						currentEntry.setRoom(room);
						entries.save(currentEntry);
						return 1;
					}
				);
			}
		}
		 */
		model.addAttribute("form", form);
		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());

		return "redirect:/rooms";
	}


	@PostMapping("/rooms/{id}/delete")
	public String deleteRoom(@PathVariable("id") long id){
		rooms.findById(id).map(room -> {	//nimmt Wert falls nicht leer, dann wird funktion delete aufgerufen
			/*for(TimeTableEntry entry : room.getEntrySet()){
				entries.delete(entry);
			}*/
				rooms.delete(room);
				return 1;
			}
		);

		return "redirect:/rooms";
	}


	@PostMapping("/rooms/{room}/delete")
	public String deleteRoom(@PathVariable("room") Room room){
		rooms.delete(room);

		return "redirect:/rooms";
	}


}
