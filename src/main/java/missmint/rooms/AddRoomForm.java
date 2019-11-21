package missmint.rooms;

import javax.validation.constraints.NotBlank;

public class AddRoomForm {
	private final @NotBlank(message="Darf nicht leer sein!") String name;

	public AddRoomForm(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public Room createRoom(EntryRepository entries, RoomRepository rooms){
		return new Room(name);
	}
}