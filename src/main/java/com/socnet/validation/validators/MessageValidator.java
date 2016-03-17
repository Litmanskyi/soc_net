package com.socnet.validation.validators;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MessageValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String message = (String) target;
        if (message.isEmpty()) {
            errors.rejectValue("message", "message.empty");
        }
        if (message.length() < 2) {
            errors.rejectValue("message", "message.size.min", "message.size.min");
        }
        if (message.length() > 1000) {
            errors.rejectValue("message", "message.size.max", "message.size.max");
        }
    }
}
