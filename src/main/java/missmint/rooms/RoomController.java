package missmint.rooms;

import org.salespointframework.useraccount.QUserAccount;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Controller
public class RoomController {

	private RoomsRepository rooms;
	private EntriesRepository entries;
	private Set<TimeTableEntry> existingEntries;

	RoomController(RoomsRepository rooms, EntriesRepository entries){
		this.rooms = rooms;
		this.entries = entries;
	}

	@GetMapping("/rooms")
	//@PreAuthorize("isAuthenticated()")
	public String showRooms(Model model) {

		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());


		return "rooms";
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

	//Entries

	@PostMapping("/rooms/{id}/showEntries")
	public String showEntries(Model model, @PathVariable("id") long id){
		 existingEntries= new HashSet<>();

		//TODO entries existieren nur, wenn gebucht worden ist
		rooms.findById(id).map(room -> {	//nimmt Wert falls nicht leer, dann wird funktion delete aufgerufen
			for (TimeTableEntry entry : room.getEntrySet())	{
				if (entry.getOrder().getInbound().equals(LocalDate.now())){
					existingEntries.add(entry);
				}
			}
			return 1;
			}
		);
		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());
		model.addAttribute("existingEntries", existingEntries);

		return "entries";
	}


	@PostMapping("/rooms/{id}/createEntry")
	public String createEntry(Model model, @PathVariable("id") long id){

		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());

		return "rooms";
	}

	@PostMapping("/rooms/{id}/deleteEntry")
	public String deleteEntry(Model model, @PathVariable("id") long id){

		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());

		return "rooms";
	}

}
