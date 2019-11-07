package missmint.rooms;

import java.util.LinkedList;
import java.util.List;

public class Room {

	private int RoomId;
	private Material UsedMaterial;
	enum Booking {FREE, BOOKED}
	private Booking booking;

	public Room (Material UsedMaterial, Booking booking){
		this.UsedMaterial = UsedMaterial;
		this.booking = booking;
	}

	//Getter
	public Material getUsedMaterial() {
		return UsedMaterial;
	}

	public Booking getBooking() {
		return booking;
	}

	public int getRoomId() {
		return RoomId;
	}

	//Setter
	public void setUsedMaterial(Material material) {
		this.UsedMaterial = material;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public void setRoomId(int roomId) {
		RoomId = roomId;
	}
}
