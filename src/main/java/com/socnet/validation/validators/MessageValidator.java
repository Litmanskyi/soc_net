package com.socnet.validation.validators;


import com.socnet.entity.Message;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Getter
@Setter

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
            errors.reject("message.empty");
        }
        if (message.length() < 2) {
            errors.reject("message.size.min");
        }
        if (message.length() > 1000) {
            errors.reject("message.size.max");
        }
    }
}
