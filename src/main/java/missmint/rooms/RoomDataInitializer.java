package missmint.rooms;

import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Component;

@Component
public class RoomDataInitializer implements DataInitializer {
	private RoomRepository roomRepository;

	public RoomDataInitializer(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	@Override
	public void initialize() {
		Room room = new Room("HSZ/0001");
		roomRepository.save(room);
	}
}
