package missmint.rooms;

import missmint.time.EntryRepository;
import missmint.time.TimeTableEntry;
import missmint.time.TimeTableService;
import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class RoomService {

	private RoomRepository roomRepository;
	private EntryRepository entryRepository;
	private final BusinessTime time;

	public RoomService(RoomRepository roomRepository, EntryRepository entryRepository, BusinessTime businessTime){
		this.roomRepository = roomRepository;
		this.entryRepository = entryRepository;
		this.time = businessTime;
	}

	public Iterator<Iterator<TimeTableEntry>> buildRoomTable(){
		List<Room> rooms = new LinkedList<>();
		roomRepository.findAll().forEach(rooms::add);

		LocalDate now = time.getTime().toLocalDate();

		return IntStream.range(0, TimeTableService.SLOTS.size()).mapToObj(slot ->
				rooms.stream().map(room ->
						entryRepository.findByDateAndSlotAndRoom(now, slot, room)
				).iterator()
		).iterator();
	}

}
