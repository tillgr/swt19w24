package missmint;

import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class Utils {
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static <T> T getOrThrow(Optional<T> optional) {
		Assert.notNull(optional, "optional must not be null");

		return optional.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
	}
}
