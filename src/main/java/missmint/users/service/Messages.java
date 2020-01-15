package missmint.users.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/**
 * For showing custom messages
 */
@Component
public class Messages {

	private MessageSourceAccessor accessor;

	public Messages(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource, LocaleContextHolder.getLocale());
	}

	/**
	 * Get the message based on the code
	 *
	 * @param code Message code
	 * @return message depending on given code
	 */
	public String get(String code) {
		return accessor.getMessage(code);
	}
}
