package com.socnet.validation.validators;


import com.socnet.entity.Message;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class MessageValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return Message.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "message", "message.size.min");
        Message message = (Message) target;
        if (message.getMessage().isEmpty()) {
            errors.rejectValue("message", "message.empty");
        }
        if (message.getMessage().length() < 2) {
            errors.rejectValue("message", "message.size.min","message.size.min");
        }
        if (message.getMessage().length() > 1000) {
            errors.rejectValue("message", "message.size.max","message.size.max");
        }
    }
}
