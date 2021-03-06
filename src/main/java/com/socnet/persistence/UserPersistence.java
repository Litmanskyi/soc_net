package com.socnet.persistence;

import com.socnet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserPersistence extends JpaRepository<User, String> {

    @Query("SELECT u FROM User AS u WHERE  u.email = ?1")
    User findUserByEmail(String email);

}