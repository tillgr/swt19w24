package missmint.time;

import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderService;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceCategory;
import missmint.orders.service.ServiceManager;
import missmint.rooms.Room;
import missmint.rooms.RoomRepository;
import missmint.users.repositories.StaffRepository;
import org.salespointframework.time.BusinessTime;
import org.springframework.data.util.Pair;
import org.springframework.data.util.StreamUtils;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Service
public class TimeTableService {
	private final ServiceManager serviceManager;
	private final BusinessTime time;
	private final OrderService orderService;
	private final RoomRepository rooms;
	private final EntryRepository entries;
	private final StaffRepository staffRepository;

	public static final List<Pair<LocalTime, LocalTime>> SLOTS = List.of(
		Pair.of(LocalTime.of(8, 0), LocalTime.of(9, 0)),
		Pair.of(LocalTime.of(11, 30), LocalTime.of(12, 30)),
		Pair.of(LocalTime.of(14, 0), LocalTime.of(15, 0)),
		Pair.of(LocalTime.of(17, 30), LocalTime.of(18, 30)),
		Pair.of(LocalTime.of(22, 0), LocalTime.of(23, 0))
	);

	public TimeTableService(ServiceManager serviceManager, BusinessTime businessTime, OrderService orderService, RoomRepository rooms, EntryRepository entries, StaffRepository staffRepository) {
		this.serviceManager = serviceManager;
		this.time = businessTime;
		this.orderService = orderService;
		this.rooms = rooms;
		this.entries = entries;
		this.staffRepository = staffRepository;
	}

	public TimeTableEntry createEntry(MissMintOrder order) {
		MissMintService service = serviceManager.getService(order);
		ServiceCategory category = ServiceManager.getCategory(service);
		Assert.isTrue(orderService.isOrderAcceptable(service), "service must be acceptable");

		LocalDateTime now = time.getTime();
		Optional<Optional<TimeTableEntry>> entry = LongStream.range(0, Long.MAX_VALUE)
			.mapToObj(now.toLocalDate()::plusDays)
			.flatMap(date ->
				IntStream.range(0, SLOTS.size())
					.mapToObj(slotIndex -> Pair.of(slotIndex, SLOTS.get(slotIndex)))
					.filter(indexedSlot -> now.isBefore(LocalDateTime.of(date, indexedSlot.getSecond().getFirst())))
					.map(indexedSlot -> {
						int slotIndex = indexedSlot.getFirst();

						return StreamUtils.createStreamFromIterator(rooms.findAll().iterator())
							.filter(room -> !entries.existsByRoomAndDateAndSlot(room, date, slotIndex))
							.findAny()
							.flatMap(room ->
								staffRepository.findAllBySkillsContaining(category)
									.filter(staff -> !entries.existsByStaffAndDateAndSlot(staff, date, slotIndex))
									.stream().findAny().map(staff -> new TimeTableEntry(order, staff, slotIndex, room, date))
							);
					})
			)
			.filter(Optional::isPresent)
			.findFirst();

		//noinspection OptionalGetWithoutIsPresent
		return entry.get().get();
	}

	public Iterator<TimeTableEntry> todaysEntries(Room room) {
		return room.getEntrySet().stream().filter(entry ->
			entry.getDate().equals(time.getTime().toLocalDate())
		).sorted(Comparator.comparingInt(TimeTableEntry::getSlot)).iterator();
	}
}
