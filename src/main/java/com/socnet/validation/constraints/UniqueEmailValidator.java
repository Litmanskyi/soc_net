package com.socnet.validation.constraints;


import com.socnet.Application;
import com.socnet.entity.User;
import com.socnet.service.UserService;
import com.socnet.validation.annotations.UniqueEmail;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, User> {

    private static final String ILLEGAL_TYPE_EXCEPTION = "Incorrect type";

    private UserService userService;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        if (userService == null) {
            userService = Application.getBean(UserService.class);
        }
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {

        String valueId = user.getId();
        String valueEmail = user.getEmail();
        User u = userService.findUserById(valueEmail);
        if(u==null){
            return true;
        }
        String userIdByEmail = u.getId();
        if(valueId != null && userIdByEmail.equals(valueId)) {
            return true;
        }
        return false;
    }
}