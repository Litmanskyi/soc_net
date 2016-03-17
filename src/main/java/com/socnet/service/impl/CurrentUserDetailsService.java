package com.socnet.service.impl;

import com.socnet.authentication.CurrentUser;
import com.socnet.entity.User;
import com.socnet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

@Service
public class CurrentUserDetailsService implements UserDetailsService { //todo move to SpringSecurity package

    @Autowired
    private UserService userService;

    Logger logger = Logger.getLogger(getClass()); //todo private

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("load user by username - " + email);
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email=%s was not found", email));
        }
        return new CurrentUser(user);
    }
}