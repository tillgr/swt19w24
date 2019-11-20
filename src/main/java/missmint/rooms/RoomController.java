package missmint.rooms;

import org.salespointframework.useraccount.QUserAccount;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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

	@GetMapping("/rooms/listEntries")
	public String listEntries(Model model){

		//TODO entries existieren nur, wenn gebucht worden ist

		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());

		return "rooms";
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
