package com.socnet.persistence;

import com.socnet.entity.Post;
import com.socnet.entity.Wall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WallPersistence extends JpaRepository<Wall, String> {

    @Query("SELECT w.posts FROM Wall AS w WHERE w.user.id = ?1")
    List<Post> getPostsByUserId(String userId);
}

