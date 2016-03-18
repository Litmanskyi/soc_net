package com.socnet.authentication;

import com.socnet.entity.User;
import com.socnet.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserDetailsService implements UserDetailsService { //todo ++ move to SpringSecurity package

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(getClass());

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