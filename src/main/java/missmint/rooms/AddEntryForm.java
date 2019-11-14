package missmint.rooms;

import javax.validation.constraints.NotBlank;
import

public class AddEntryForm {
	private final @NotBlank(message="Darf nicht leer sein!") Room room;
	private final @NotBlank(message="Darf nicht leer sein!") int start;
	private final @NotBlank(message="Darf nicht leer sein!") int end;

	public AddEntryForm(Room room, int start, int end){
		this.room = room;
		this.start = start;
		this.end = end;
	}



	public TimeTableEntry createEntry(){
		return new TimeTableEntry(start, end, room);
	}
}
