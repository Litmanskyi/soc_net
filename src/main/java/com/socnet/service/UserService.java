package com.socnet.service;


import com.socnet.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    void deleteUser();

    User updatePassword(User updUser);

    User findUserById(String id);

    User findUserByEmail(String email);

    List<User> findAllUsers();

    List<User> findUsersByIds(Set<String> usersIds);
}
