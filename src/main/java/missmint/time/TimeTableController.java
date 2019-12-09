package missmint.time;

import missmint.Utils;
import missmint.rooms.Room;
import missmint.rooms.RoomRepository;
import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TimeTableController {

	private TimeTableService timeService;
	private RoomRepository roomRepository;
	private final BusinessTime time;

	public TimeTableController(TimeTableService timeService, RoomRepository roomRepository, BusinessTime businessTime) {
		this.timeService = timeService;
		this.roomRepository = roomRepository;
		this.time = businessTime;
		//TODO businessTime in constructor
	}

	@GetMapping("/rooms/showEntries")
	public String showEntries(Model model) {
		//Room room = Utils.getOrThrow(roomRepository.findById(id));
		//List of Arrays
		List<TimeTableEntry[]> tableList = new ArrayList<>();


		List<Room> roomList= new LinkedList<>();

		List<TimeTableEntry> sortedSet;

		//put rooms into list
		roomRepository.findAll().forEach(roomList :: add);
		//iterate rooms
		for (Room room : roomList){
			TimeTableEntry [] helpArray = new TimeTableEntry[5];
			//iterate slots
			for (int j= 0; j < 5; j++){
				//create sorted entrySet
				sortedSet = room.getEntrySet().stream().filter(entry ->
					entry.getDate().equals(time.getTime().toLocalDate())
				).sorted(Comparator.comparingInt(TimeTableEntry::getSlot)).collect(Collectors.toList());
				//if slot matches slot id of entry
				for (TimeTableEntry entry : sortedSet){
					if (j == entry.getSlot()){
						//add entry to helpArray
						helpArray[j]= entry;
					}
				}
			}
			tableList.add(helpArray);
		}

		//model.addAttribute("existingEntries", timeService.todaysEntries(room));

		model.addAttribute("tableList", tableList);
		model.addAttribute("roomRepository", roomRepository.findAll());

		return "entries";
	}
}
