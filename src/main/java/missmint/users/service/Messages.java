package missmint.users.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class Messages {

	private MessageSourceAccessor accessor;

	public Messages(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource, LocaleContextHolder.getLocale());
	}

	public String get(String code) {
		return accessor.getMessage(code);
	}
}
