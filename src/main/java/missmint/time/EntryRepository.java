package missmint.time;

import missmint.rooms.Room;
import missmint.users.model.Staff;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface EntryRepository extends CrudRepository<TimeTableEntry, Long> {
	boolean existsByRoomAndDateAndSlot(Room room, LocalDate date, int slot);
	boolean existsByStaffAndDateAndSlot(Staff staff, LocalDate date, int slot);
}
