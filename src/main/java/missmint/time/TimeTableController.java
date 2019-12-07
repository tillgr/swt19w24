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

	@GetMapping("/rooms/showEntries")
	public String showEntries(Model model) {
		//Room room = Utils.getOrThrow(roomRepository.findById(id));

		//model.addAttribute("existingEntries", timeService.todaysEntries(room));
		int[] a = new int[3];
		a[0] = 1;
		a[1] = 2;
		a[2] = 3;
		model.addAttribute("roomRepository", a);

		return "entries";
	}
}
