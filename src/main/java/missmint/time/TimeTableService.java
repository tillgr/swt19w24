package missmint.time;

import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderService;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceCategory;
import missmint.orders.service.ServiceService;
import missmint.rooms.Room;
import missmint.rooms.RoomRepository;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
import org.salespointframework.time.BusinessTime;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TimeTableService {
	private final ServiceService serviceService;
	private final BusinessTime time;
	private final OrderService orderService;
	private final RoomRepository rooms;
	private final EntryRepository entries;
	private final StaffRepository staffRepository;

	public static final List<Pair<LocalTime, LocalTime>> SLOTS = List.of(
		Pair.of(LocalTime.of(8, 0), LocalTime.of(9, 0)),
		Pair.of(LocalTime.of(9, 30), LocalTime.of(10, 30)),
		Pair.of(LocalTime.of(11, 0), LocalTime.of(12, 0)),
		Pair.of(LocalTime.of(12, 30), LocalTime.of(13, 30)),
		Pair.of(LocalTime.of(14, 0), LocalTime.of(15, 0))
	);

	public TimeTableService(ServiceService serviceService, BusinessTime businessTime, OrderService orderService, RoomRepository rooms, EntryRepository entries, StaffRepository staffRepository) {
		this.serviceService = serviceService;
		this.time = businessTime;
		this.orderService = orderService;
		this.rooms = rooms;
		this.entries = entries;
		this.staffRepository = staffRepository;
	}

	public void createEntry(MissMintOrder order) {
		MissMintService service = serviceService.getService(order);
		ServiceCategory category = ServiceService.getCategory(service);
		Assert.isTrue(orderService.isOrderAcceptable(service), "service must be acceptable");

		LocalDate date = time.getTime().toLocalDate();
		while (true) {
			for (int slotIndex = 0; slotIndex < SLOTS.size(); slotIndex++) {
				Pair<LocalTime, LocalTime> slot = SLOTS.get(slotIndex);

				if (time.getTime().isAfter(LocalDateTime.of(date, slot.getSecond()))) {
					continue;
				}

				for (Room room : rooms.findAll()) {
					if (entries.existsByRoomAndDateAndSlot(room, date, slotIndex)) {
						continue;
					}

					for (Staff staff : staffRepository.findAllBySkillsContaining(category)) {
						if (entries.existsByStaffAndDateAndSlot(staff, date, slotIndex)) {
							continue;
						}

						TimeTableEntry entry = new TimeTableEntry(order, staff, slotIndex, room, date);
						entries.save(entry);
						return;
					}
				}
			}
			date = date.plusDays(1);
		}
	}
}
