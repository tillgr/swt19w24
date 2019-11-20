package missmint.rooms;

import missmint.users.service.StaffManagement;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class RoomInitializer implements DataInitializer {

	private EntriesRepository entries;
	private RoomsRepository rooms;

	public RoomInitializer(EntriesRepository entries, RoomsRepository rooms) {

		Assert.notNull(entries, "UserAccountManager must not be null");
		Assert.notNull(rooms, "UserManagement must not be null");

		this.entries = entries;
		this.rooms = rooms;
	}

	@Override
	public void initialize() {

		Room room1 = new Room("Room1", entries, rooms);
		Room room2 = new Room("Room2", entries, rooms);
		Room room3 = new Room("Room3", entries, rooms);
		Room room4 = new Room("Room4", entries, rooms);
		Room room5 = new Room("Room5", entries, rooms);
		Room room6 = new Room("Room6", entries, rooms);
		Room room7 = new Room("Room7", entries, rooms);
	}

	}
