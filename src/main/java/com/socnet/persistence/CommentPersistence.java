package com.socnet.persistence;

import com.socnet.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentPersistence extends JpaRepository<Comment,String>{
}
