package missmint;

import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.Optional;

public class Utils {
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static <T> T getOrThrow(Optional<T> optional) {
		Assert.notNull(optional, "optional must not be null");

		return optional.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	public static void flushAndClear(EntityManager manager) {
		Session session = manager.unwrap(Session.class);
		if (!session.isDirty()) {
			return;
		}
		session.flush();
		session.clear();
	}
}
