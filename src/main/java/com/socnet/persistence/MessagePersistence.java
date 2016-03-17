package com.socnet.persistence;

import com.socnet.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessagePersistence extends JpaRepository<Message, String> {
}
