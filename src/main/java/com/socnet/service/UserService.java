package com.socnet.service;


import com.socnet.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> findAllUsers();

    void deleteUser();

    User updateUser(User user);

    User createUser(User user);

    User findUserById(String id);

    User findUserByEmail(String email);

    User updatePassword(User updUser);

    List<User> findUsersByIds(Set<String> usersIds);
}
