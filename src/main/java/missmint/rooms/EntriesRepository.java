package missmint.rooms;

import org.springframework.data.repository.CrudRepository;

public interface EntriesRepository extends CrudRepository<TimeTableEntry, Long> {
}
