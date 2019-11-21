package missmint.rooms;

import org.springframework.data.repository.CrudRepository;

public interface EntryRepository extends CrudRepository<TimeTableEntry, Long> {
}
