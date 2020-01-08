package missmint.rooms;

import missmint.time.EntryRepository;

import javax.validation.constraints.NotBlank;

public class AddRoomForm {
	/**
	 * The name of the room.
	 *
	 * This value must not be blank.
	 */
	@NotBlank(message = "Darf nicht leer sein!")
	private final String name;

	public AddRoomForm(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Room createRoom() {
		return new Room(name);
	}
}