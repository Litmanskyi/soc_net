package com.socnet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.socnet.entity.Relation;
import com.socnet.entity.User;
import com.socnet.service.RelationService;
import com.socnet.utility.AuthenticatedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class RelationController {

    @Autowired
    private RelationService relationService;

    @RequestMapping(value = "/friend/{id}", method = RequestMethod.POST)
    @JsonView(Relation.RelationView.class)
    public Relation addToFriend(@PathVariable("id") String targetId) {
        return relationService.addFriend(targetId);
    }

    @RequestMapping(value = "/friend/{id}", method = RequestMethod.DELETE)
    public void deleteFriend(@PathVariable("id") String userId) {
        relationService.deleteFriend(userId);
    }

    @RequestMapping(value = "/blacklist/{id}", method = RequestMethod.POST)
    @JsonView(Relation.RelationView.class)
    public Relation addToBlacklist(@PathVariable("id") String userId) {
        return relationService.addToBlacklist(userId);
    }

    @RequestMapping(value = "/blacklist/{id}", method = RequestMethod.DELETE)
    public void deleteFromBlacklist(@PathVariable("id") String userId) {
        relationService.deleteFromBlacklist(userId);
    }

    @RequestMapping(value = "/friends/", method = RequestMethod.GET)
    @JsonView(User.UserView.class)
    public List<User> findFriends() {
        return relationService.findFriends(AuthenticatedUtils.getCurrentAuthUser().getId());
    }

    @RequestMapping(value = "/relations/{id}", method = RequestMethod.GET)
    @JsonView(User.UserView.class)
    public List<User> findFriendsById(@PathVariable("id") String userId) {
        return relationService.findFriends(userId);
    }

    @RequestMapping(value = "/blacklist/", method = RequestMethod.GET)
    @JsonView(User.UserView.class)
    public List<User> findFriendsInBlacklist() {
        return relationService.findBlacklist(AuthenticatedUtils.getCurrentAuthUser().getId());
    }

    @RequestMapping(value = "/followers/", method = RequestMethod.GET)
    @JsonView(User.UserView.class)
    public List<User> findFollowers() {
        return relationService.findFollowers(AuthenticatedUtils.getCurrentAuthUser().getId());
    }

    @RequestMapping(value = "/followers/{id}", method = RequestMethod.GET)
    @JsonView(User.UserView.class)
    public List<User> findFollowersById(@PathVariable("id") String userId) {
        return relationService.findFollowers(userId);
    }

}
