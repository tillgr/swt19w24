package missmint.time;

import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderService;
import missmint.orders.order.OrderState;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceCategory;
import missmint.orders.service.ServiceManager;
import missmint.rooms.Room;
import missmint.rooms.RoomRepository;
import missmint.users.repositories.StaffRepository;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Service
public class TimeTableService {
	private final ServiceManager serviceManager;
	private final BusinessTime time;
	private final OrderService orderService;
	private final RoomRepository rooms;
	private final EntryRepository entries;
	private final StaffRepository staffRepository;
	private OrderManager<MissMintOrder> orderManager;

	public static final List<Pair<LocalTime, LocalTime>> SLOTS = List.of(
		Pair.of(LocalTime.of(8, 0), LocalTime.of(9, 0)),
		Pair.of(LocalTime.of(11, 30), LocalTime.of(12, 30)),
		Pair.of(LocalTime.of(14, 0), LocalTime.of(15, 0)),
		Pair.of(LocalTime.of(17, 30), LocalTime.of(18, 30)),
		Pair.of(LocalTime.of(22, 0), LocalTime.of(23, 0))
	);

	public TimeTableService(ServiceManager serviceManager, BusinessTime businessTime, OrderService orderService, RoomRepository rooms, EntryRepository entries, StaffRepository staffRepository, OrderManager<MissMintOrder> orderManager) {
		this.serviceManager = serviceManager;
		this.time = businessTime;
		this.orderService = orderService;
		this.rooms = rooms;
		this.entries = entries;
		this.staffRepository = staffRepository;
		this.orderManager = orderManager;
	}

	private Stream<LocalDate> dateStream() {
		LocalDateTime now = time.getTime();
		return LongStream.range(0, Long.MAX_VALUE).mapToObj(now.toLocalDate()::plusDays);
	}

	private Stream<Pair<Integer, Pair<LocalTime, LocalTime>>> slotStream(LocalDate date) {
		LocalDateTime now = time.getTime();
		return IntStream.range(0, SLOTS.size())
			.mapToObj(slotIndex -> Pair.of(slotIndex, SLOTS.get(slotIndex)))
			.filter(indexedSlot -> now.isBefore(LocalDateTime.of(date, indexedSlot.getSecond().getFirst())));
	}

	public void rebuildTimeTable() {
		LocalDateTime now = time.getTime();
		entries.findAllByDateAfter(now.toLocalDate().minusDays(1))
			.filter(entry -> now.isBefore(entry.getBeginning()))
			.forEach(entries::delete);

		orderManager.findAll(Pageable.unpaged())
			.filter(order -> order.getOrderState() == OrderState.WAITING)
			.filter(order -> orderService.isOrderAcceptable(serviceManager.getService(order)))
			.get().sorted(Comparator.comparing(MissMintOrder::getExpectedFinished))
			.forEach(order -> {
				Assert.isNull(order.getEntry(), "after removing time table entries the entry field should be null");
				entries.save(createEntry(order));
			});

	}

	public TimeTableEntry createEntry(MissMintOrder order) {
		MissMintService service = serviceManager.getService(order);
		ServiceCategory category = ServiceManager.getCategory(service);
		Assert.isTrue(orderService.isOrderAcceptable(service), "service must be acceptable");

		Optional<Optional<TimeTableEntry>> entry = dateStream().flatMap(date ->
			slotStream(date).map(indexedSlot -> {
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
		return room.getEntrySet().stream().filter(entry -> entry.getDate().equals(time.getTime().toLocalDate())).iterator();
	}
}
