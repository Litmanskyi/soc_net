package com.socnet.persistence;

import com.socnet.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostPersistence extends JpaRepository<Post, String> {
}
