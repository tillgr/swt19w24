package missmint.rooms;


public class TimeTable {

	private TimeTableEntry entry;

	public TimeTable (TimeTableEntry entry){
		this.entry = entry;
	}

	//Getter
	public TimeTableEntry getEntry() {
		return entry;
	}

	//Setter
	public void setEntry(TimeTableEntry entry) {
		this.entry = entry;
	}

}
