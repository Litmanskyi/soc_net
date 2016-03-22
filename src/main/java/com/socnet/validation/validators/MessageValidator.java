package com.socnet.validation.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;

@Component
public class MessageValidator implements Validator {

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String message = (String) target;
        if (message.isEmpty()) {
            errors.reject("message.empty", getMessage("message.empty", "en"));
        }
        if (message.length() < 2) {
            errors.reject("message.size.min", getMessage("message.size.min", "en"));
        }
        if (message.length() > 1000) {
            errors.reject("message.size.max", getMessage("message.size.max", "en"));
        }
    }

    public String getMessage(String code, String locale) {
        return messageSource.getMessage(code, null, new Locale(locale));
    }
}
