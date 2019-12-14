package missmint.rooms;

import missmint.time.EntryRepository;
import missmint.time.TimeTableEntry;
import missmint.time.TimeTableService;
import org.salespointframework.time.BusinessTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
public class RoomController {
	private RoomRepository roomRepository;
	private TimeTableService timeService;
	private RoomService roomService;
	private final BusinessTime time;
	private EntryRepository entryRepository;

	RoomController(RoomRepository roomRepository, TimeTableService timeService, EntryRepository entryRepository, BusinessTime businessTime, RoomService roomService) {
		this.roomRepository = roomRepository;
		this.timeService = timeService;
		this.entryRepository = entryRepository;
		this.time = businessTime;
		this.roomService = roomService;
	}

	@GetMapping("/rooms")
	@PreAuthorize("isAuthenticated()")
	public String showRooms(Model model) {

		model.addAttribute("slotTable", roomService.buildRoomTable());
		model.addAttribute("rooms", roomRepository.findAll());

		return "rooms";
	}

	@PostMapping("/rooms/add")
	public String addRoom(Model model, @Valid @ModelAttribute("form") AddRoomForm form, Errors errors) {
		if (errors.hasErrors()) {
			return showRooms(model);
		}

		roomRepository.save(form.createRoom());

		return "redirect:/rooms";
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@PostMapping("/rooms/{room}/delete")
	public String deleteRoom(@PathVariable("room") Optional<Room> optionalRoom) {
		// TODO handle entries
		optionalRoom.ifPresent(room -> {
			roomRepository.delete(room);
		});

		return "redirect:/rooms";
	}
}
