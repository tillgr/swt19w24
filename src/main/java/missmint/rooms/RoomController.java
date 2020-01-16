package missmint.rooms;
import missmint.time.TimeTableService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Controller for displaying and managing rooms.
 */
@Controller
public class RoomController {
	private RoomRepository rooms;
	private TimeTableService service;
	private RoomService roomService;

	/**
	 * create new room
	 * @param rooms rooms from the repository
	 * @param service service which handles entries
	 */
	RoomController(RoomRepository rooms, TimeTableService service, RoomService roomService) {
		this.rooms = rooms;
		this.service = service;
		this.roomService = roomService;
	}

	/**
	 * show currently existing rooms
	 * @param model Spring model
	 * @param form Spring form
	 * @return rooms page
	 */
	@GetMapping("/rooms")
	@PreAuthorize("isAuthenticated()")
	public String showRooms(Model model, @ModelAttribute("form") AddRoomForm form , Authentication auth) {
		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("slotTable", roomService.buildRoomTable());
		model.addAttribute("auth", auth.getName());
		model.addAttribute("times", TimeTableService.SLOTS);
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
			return "redirect:/rooms";
		}

		if (!rooms.existsByName(form.getName())){
			rooms.save(form.createRoom());
			service.rebuildTimeTable();
			return "redirect:/rooms";
		}

		else {
			errors.rejectValue("name", "rooms.add.name.exists");
			return "redirect:/rooms";
		}
	}

	/**
	 * delete existing rooms from the repository
	 * @param optionalRoom the room which should be deleted
	 * @return redirect to the rooms page
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/rooms/{room}/delete")
	public String deleteRoom(@PathVariable("room") Long optionalRoom) {

		rooms.findById(optionalRoom).ifPresent(room -> rooms.delete(room));
		service.rebuildTimeTable();

		return "redirect:/rooms";
	}
}
