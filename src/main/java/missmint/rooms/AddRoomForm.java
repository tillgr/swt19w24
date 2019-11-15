package missmint.rooms;

import javax.validation.constraints.NotBlank;

public class AddRoomForm {
	private final @NotBlank(message="Darf nicht leer sein!") String name;
	private final @NotBlank(message="Darf nicht leer sein!") String belegung;

	public AddRoomForm(String name, String belegung){
		this.name = name;
		this.belegung = belegung;
	}

	public String getName(){
		return name;
	}

	public String getBelegung() {
		return belegung;
	}

	public Room createRoom(EntriesRepository entries, RoomsRepository rooms){
		return new Room(name, belegung, entries, rooms);
	}
}
