package missmint.rooms;

import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Component;

/**
 * Initializes the repository with data for the rooms.
 */
@Component
public class RoomDataInitializer implements DataInitializer {
	private RoomRepository roomRepository;

	public RoomDataInitializer(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	@Override
	public void initialize() {
		Room room = new Room("HSZ/0001");
		Room room1 = new Room("HSZ/0002");
		Room room2 = new Room("HSZ/0003");

		roomRepository.save(room);
		roomRepository.save(room1);
		roomRepository.save(room2);
	}
}
