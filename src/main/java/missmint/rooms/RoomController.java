package missmint.rooms;

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
		AddRoomForm form = new AddRoomForm("", "");

		/*
		Room room1 = new Room("Raum1", "Dienstleistung A");
		Room room2 = new Room("Raum2", "Dienstleistung B");
		Room room3 = new Room("Raum3", "Dienstleistung C");
		Room room4 = new Room("Raum4", "Dienstleistung D");
		Room room5 = new Room("Raum5", "Dienstleistung E");
		Room room6 = new Room("Raum6", "Dienstleistung F");

		rooms.save(room1);
		rooms.save(room2);
		rooms.save(room3);
		rooms.save(room4);
		rooms.save(room5);
		rooms.save(room6);

		TimeTableEntry entry1 = new TimeTableEntry("1","2", room1);
		TimeTableEntry entry2 = new TimeTableEntry("2","3", room2);
		TimeTableEntry entry3 = new TimeTableEntry("3","4",room3);
		TimeTableEntry entry4 = new TimeTableEntry("4","5",room4);
		TimeTableEntry entry5 = new TimeTableEntry("5","6",room5);
		TimeTableEntry entry6 = new TimeTableEntry("6","7", room6);

		entries.save(entry1);
		entries.save(entry2);
		entries.save(entry3);
		entries.save(entry4);
		entries.save(entry5);
		entries.save(entry6);
		*/

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

		model.addAttribute("form", form);
		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());


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


	@GetMapping("/rooms/listFreeSlots")
	public String listFreeSlots(Model model){
		Iterator<TimeTableEntry> it = entries.findAll().iterator();

		AddRoomForm form = new AddRoomForm("", "");
		List<TimeTableEntry> freeSlots = new LinkedList<>();

		while(it.hasNext()){
			TimeTableEntry currentEntry = it.next();
			if(currentEntry.getBooking().equals(Booking.FREE)){
				freeSlots.add(currentEntry);
			}
		}

		model.addAttribute("form", form);
		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());
		model.addAttribute("freeSlots", freeSlots);

		return "rooms";
	}


	@PostMapping("/rooms/{id}/bookFreeSlot")
	public String bookFreeSlot(Model model, @PathVariable("id") long id){
		Iterator<TimeTableEntry> it = entries.findAll().iterator();

		AddRoomForm form = new AddRoomForm("", "");

		while(it.hasNext()){
			TimeTableEntry currentEntry = it.next();
			if(currentEntry.getBooking().equals(Booking.FREE) && rooms.findById(id).map(room -> {return currentEntry.getRoom().equals(room); }).orElse(false)){
				currentEntry.setBooking(Booking.BOOKED);
				rooms.findById(id).map(room -> {
					currentEntry.setRoom(room);
					entries.save(currentEntry);
					return 1;
				}
				);
				break;
			}
		}

		model.addAttribute("form", form);
		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());

		return "rooms";
	}


	@PostMapping("/rooms/addEntry")
	public String addEntry(Model model, Room room){
		Iterator<TimeTableEntry> it = entries.findAll().iterator();
		int time = 0;
		while(it.hasNext()){
			TimeTableEntry currentEntry = it.next();
			if(currentEntry.getEnd()>time){
				time = currentEntry.getEnd();
			}
		}
		TimeTableEntry entry = new TimeTableEntry(time, time + 1, room);
		entries.save(entry);

		return "redirect:/rooms";
	}

	@PostMapping("/rooms/entries/{id}/delete")
	public String deleteEntry(@PathVariable("id") long id){
		entries.findById(id).map(entry -> {	//nimmt Wert falls nicht leer, dann wird funktion delete aufgerufen
				entries.delete(entry);
				return 1;
			}
		);

		return "redirect:/rooms";
	}



}
