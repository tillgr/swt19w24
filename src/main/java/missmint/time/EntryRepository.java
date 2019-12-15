package missmint.time;

import missmint.orders.order.MissMintOrder;
import missmint.rooms.Room;
import missmint.users.model.Staff;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.time.LocalDate;

/**
 * repository which stores the entries.
 */
public interface EntryRepository extends CrudRepository<TimeTableEntry, Long> {
	boolean existsByRoomAndDateAndSlot(Room room, LocalDate date, int slot);
	boolean existsByStaffAndDateAndSlot(Staff staff, LocalDate date, int slot);
	Streamable<TimeTableEntry> findAllByDateAfter(LocalDate date);
	void deleteTimeTableEntriesByStaff(Staff staff);
	void deleteTimeTableEntriesByOrder(MissMintOrder order);
	Streamable<TimeTableEntry> findAllByStaff(Staff staff);
	TimeTableEntry findByOrder(MissMintOrder order);
}
