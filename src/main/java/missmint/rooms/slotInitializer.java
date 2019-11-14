package missmint.rooms;

import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(1)
public class slotInitializer implements DataInitializer {

	private EntriesRepository entries;

	slotInitializer(EntriesRepository entries) {

		Assert.notNull(entries, "entries must not be null!");

		this.entries = entries;
	}

	@Override
	public void initialize(){

		if (entries.findAll().iterator().hasNext()) {
			return;
		}
		/*
		TimeTableEntry entry1 = new TimeTableEntry(1,2, null);
		TimeTableEntry entry2 = new TimeTableEntry(2,3, null);
		TimeTableEntry entry3 = new TimeTableEntry(3,4,null);
		TimeTableEntry entry4 = new TimeTableEntry(4,5,null);
		TimeTableEntry entry5 = new TimeTableEntry(5,6,null);
		TimeTableEntry entry6 = new TimeTableEntry(6,7, null);

		entries.save(entry1);
		entries.save(entry2);
		entries.save(entry3);
		entries.save(entry4);
		entries.save(entry5);
		entries.save(entry6);

		 */

	}

}
