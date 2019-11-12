package missmint.rooms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Room {

	private @Id @GeneratedValue long id;
	//private Material UsedMaterial;
	enum Booking {FREE, BOOKED}
	private Booking booking;
	private String name;
	private String belegung;

	public Room (String name, String belegung){
		//this.UsedMaterial = UsedMaterial;
		this.booking = Booking.FREE;
		this.name = name;
		this.belegung = belegung;
	}

	private Room (){
		//this.UsedMaterial = UsedMaterial;
		this.booking = null;
		this.name = null;
		this.belegung = null;
	}

	//Getter
	/*public Material getUsedMaterial() {
		return UsedMaterial;
	}*/

	public Booking getBooking() {
		return booking;
	}

	/*public int getRoomId() {
		return RoomId;
	}*/

	public String getBelegung() {
		return belegung;
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}

	//Setter
	/*public void setUsedMaterial(Material material) {
		this.UsedMaterial = material;
	}*/

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	/*public void setRoomId(int roomId) {
		RoomId = roomId;
	}*/
}
