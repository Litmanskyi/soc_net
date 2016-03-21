package com.socnet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.User;
import com.socnet.service.UserService;
import com.socnet.utility.AuthenticatedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteUser() {
        userService.deleteUser();
    }

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(User.UserView.class)
    public
    @ResponseBody
    List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @JsonView(User.UserView.class)
    public
    @ResponseBody
    User findUser(@PathVariable(value = "id") String id) {
        return userService.findUserById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @JsonView(User.UserView.class)
    public User createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @JsonView(User.UserView.class)
    public User updateUser(@RequestBody @Valid User user) {
        return userService.updateUser(user);

    }

    @RequestMapping(value = "/password", method = RequestMethod.PUT, consumes = "application/json")
    @JsonView(User.UserView.class)
    public User updatePassword(@RequestBody User user) {
        return userService.updatePassword(user);
    }

    @RequestMapping(value = "/current")
    @JsonView(User.UserView.class)
    public User currentUser() {
        return AuthenticatedUtils.getCurrentAuthUser();
    }

}

