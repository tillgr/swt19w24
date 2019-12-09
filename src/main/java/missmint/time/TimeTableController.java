package missmint.time;

import missmint.Utils;
import missmint.rooms.Room;
import missmint.rooms.RoomRepository;
import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Controller
public class TimeTableController {

	private TimeTableService timeService;
	private RoomRepository roomRepository;
	private final BusinessTime time;
	private EntryRepository entryRepository;

	public TimeTableController(TimeTableService timeService, RoomRepository roomRepository, BusinessTime businessTime, EntryRepository entryRepository) {
		this.timeService = timeService;
		this.roomRepository = roomRepository;
		this.time = businessTime;
		this.entryRepository = entryRepository;
	}

	@GetMapping("/rooms/showEntries")
	public String showEntries(Model model) {
		/*
		List<Room> rooms = new LinkedList<>();
		roomRepository.findAll().forEach(rooms::add);

		LocalDate now = time.getTime().toLocalDate();

		Iterator<Iterator<TimeTableEntry>> slotTable = IntStream.range(0, TimeTableService.SLOTS.size()).mapToObj(slot ->
			rooms.stream().map(room ->
				entryRepository.findByDateAndSlotAndRoom(now, slot, room)
			).iterator()
		).iterator();

		model.addAttribute("slotTable", slotTable);
		model.addAttribute("rooms", roomRepository.findAll());

		 */

		return "entries";
	}
}
