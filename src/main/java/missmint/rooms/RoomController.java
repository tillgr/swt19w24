package missmint.rooms;

import missmint.time.EntryRepository;
import missmint.time.TimeTableService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Controller for displaying and managing rooms.
 */
@Controller
public class RoomController {
	private RoomRepository rooms;
	private TimeTableService service;

	/**
	 * create new room
	 * @param rooms rooms from the repository
	 * @param service service which handles entries
	 */
	RoomController(RoomRepository rooms, TimeTableService service) {
		this.rooms = rooms;
		this.service = service;
	}

	/**
	 * show currently existing rooms
	 * @param model Spring model
	 * @param form Spring form
	 * @return rooms page
	 */
	@GetMapping("/rooms")
	@PreAuthorize("isAuthenticated()")
	public String showRooms(Model model, @ModelAttribute("form") AddRoomForm form) {
		model.addAttribute("rooms", rooms.findAll());

		return "rooms";
	}

	/**
	 * add a new room to the repository
	 * @param model Spring model
	 * @param form Spring form
	 * @param errors Spring errors
	 * @return redirect to the rooms page
	 */
	@PostMapping("/rooms/add")
	public String addRoom(Model model, @Valid @ModelAttribute("form") AddRoomForm form, Errors errors) {
		if (errors.hasErrors()) {
			return showRooms(model, form);
		}

		rooms.save(form.createRoom());
		service.rebuildTimeTable();

		return "redirect:/rooms";
	}

	/**
	 * delete existing rooms from the repository
	 * @param optionalRoom the room which should be deleted
	 * @return redirect to the rooms page
	 */
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@PostMapping("/rooms/{room}/delete")
	public String deleteRoom(@PathVariable("room") Optional<Room> optionalRoom) {
		// TODO handle entries
		optionalRoom.ifPresent(room -> {
			rooms.delete(room);
		});
		service.rebuildTimeTable();

		return "redirect:/rooms";
	}
}
