package missmint.rooms;

import javax.validation.constraints.NotBlank;

public class AddRoomForm {
	private final @NotBlank(message="Darf nicht leer sein!") String name;
	private final @NotBlank(message="Darf nicht leer sein!") String service;

	public AddRoomForm(String name, String service){
		this.name = name;
		this.service = service;
	}

	public String getName(){
		return name;
	}

	public String getService() {
		return service;
	}

	public Room createRoom(EntriesRepository entries, RoomsRepository rooms){
		return new Room(name, service, entries, rooms);
	}
}
