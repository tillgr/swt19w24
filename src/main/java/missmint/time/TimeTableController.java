package missmint.time;

import missmint.Utils;
import missmint.rooms.Room;
import missmint.rooms.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TimeTableController {

	private TimeTableService timeService;
	private RoomRepository roomRepository;

	public TimeTableController(TimeTableService timeService, RoomRepository roomRepository) {
		this.timeService = timeService;
		this.roomRepository = roomRepository;
	}

	@GetMapping("/rooms/{id}/showEntries")
	public String showEntries(Model model, @PathVariable("id") long id) {
		Room room = Utils.getOrThrow(roomRepository.findById(id));

		model.addAttribute("existingEntries", timeService.todaysEntries(room));

		return "entries";
	}
}
