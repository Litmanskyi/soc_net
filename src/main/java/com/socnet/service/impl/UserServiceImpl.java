package com.socnet.service.impl;

import com.socnet.entity.User;
import com.socnet.entity.Wall;
import com.socnet.persistence.UserPersistence;
import com.socnet.service.UserService;
import com.socnet.utility.AuthenticatedUtils;
import com.socnet.utility.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    final String DELETE_LOG = "User with name: %s, surname %s and id: %s was deleted";
    String SAVE_LOG = "User with name: %s, surname %s, email %s was saved";
    private static final String NOT_EXISTING_ID_EXCEPTION = "Haven`t user with this id";
    static final String NOT_EQUAL_SESSION_USER_WITH_SENDER = "Session's userId not equals sender's id";
    public static final String USER_EXIST = "User exist!";
    protected static final String FIELD_EMAIL_IS_NOT_FILLED = "Field email isn't filled!";
    volatile static String AVATAR_NOT_FOUND = "Avatar not found!";

    @Autowired
    private UserPersistence userPersistence;

    private Logger logger = Logger.getLogger(UserServiceImpl.class);


    public List<User> findAllUsers() {
        return userPersistence.findAll();
    }

    @Transactional
    @Override
    //todo do not delete entity forever
    public void deleteUser() {
        User user = AuthenticatedUtils.getCurrentAuthUser();
        userPersistence.delete(user.getId());
        logger.info(String.format(DELETE_LOG, user.getFirstName(), user.getLastName(), user.getId()));
    }

    /**
     * @param updUser Object user with new settings for user
     * @return Object updated user
     */
    @Override
    @Transactional
    public User updateUser(User updUser) {

        User sessionUser = AuthenticatedUtils.getCurrentAuthUser();

        if (!sessionUser.getId().equals(updUser.getId())) {
            throw new IllegalArgumentException(NOT_EQUAL_SESSION_USER_WITH_SENDER);
        }

        sessionUser.setFirstName(updUser.getFirstName());
        sessionUser.setLastName(updUser.getLastName());
        sessionUser.setEmail(updUser.getEmail());
        sessionUser = userPersistence.save(sessionUser);

        logger.info(String.format(SAVE_LOG, sessionUser.getFirstName(), sessionUser.getLastName(), sessionUser.getEmail()));
        return sessionUser;
    }

    @Override
    @Transactional
    public User createUser(User user) {

        if (user.getId() != null) {
            throw new IllegalArgumentException(USER_EXIST);
        }

        if (user.getEmail() == null) {
            throw new IllegalArgumentException(FIELD_EMAIL_IS_NOT_FILLED);
        }
        user.setEmail(user.getEmail().toLowerCase());

        //todo +++ PrePersist it!
        user = userPersistence.save(user);

        logger.info(String.format(SAVE_LOG, user.getFirstName(), user.getLastName(), user.getEmail()));
        return user;
    }

    @Override
    public User findUserById(String id) {
        return userPersistence.findOne(id);
    }

    @Override
    public User findUserByEmail(String email) {
        return userPersistence.findUserByEmail(email.toLowerCase());
    }

    @Override
    @Transactional
    public User updatePassword(User updUser) {//todo string in params
        User user = AuthenticatedUtils.getCurrentAuthUser();
        if (!user.getId().equals(updUser.getId())) {
            throw new IllegalArgumentException(NOT_EQUAL_SESSION_USER_WITH_SENDER);
        }
        user.setPassword(updUser.getPassword());
        return userPersistence.save(user);
    }

}
