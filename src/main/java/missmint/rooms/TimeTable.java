package missmint.rooms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TimeTable {
	private List<Room> roomList = new LinkedList<>();
	private List <TimeTableEntry> entries = new ArrayList <>();
	private TimeTableEntry entry;
	private List<TimeTableEntry> freeSlots = new LinkedList<>();

	public TimeTable (TimeTableEntry entry, List<Room> roomList, List<TimeTableEntry> entries){
		this.entry = entry;
		this.roomList = roomList;
		this.entries = entries;
	}

	//Methoden
	public void addEntry(){
		//eingabe verknüpfung
		entries.add(entry);
	}

	public void addRoom(Room room){
		roomList.add(room);
	}

	public void bookFreeRoom(){

		for (int i = 0; i < entries.size(); i++){
			if (entries.get(i).getRoom().getBooking() == Room.Booking.FREE){
				entries.get(i).getRoom().setBooking(Room.Booking.BOOKED);
				break;
			}
		}
		//Liste geben lassen
		//ersten freien nehmen
		//entry erstellen
		//entry in den timetable
	}

	public List<TimeTableEntry> listFreeSlots(){
		for (int i = 0; i < entries.size(); i++){
			if (entries.get(i).getRoom().getBooking() == Room.Booking.FREE){
				freeSlots.add(entries.get(i));
			}
		}
		return freeSlots;
	}

	//Getter
	public TimeTableEntry getEntry() {
		return entry;
	}

	public List<Room> getRoomList() {
		return roomList;
	}

	//Setter
	public void setEntry(TimeTableEntry entry) {
		this.entry = entry;
	}

}
