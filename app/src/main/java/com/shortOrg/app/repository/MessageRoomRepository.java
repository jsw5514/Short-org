package com.shortOrg.app.repository;

import com.shortOrg.app.domain.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRoomRepository extends JpaRepository<MessageRoom,Long> {
    List<MessageRoom> findByUser1_IdOrUser2_Id(String user1Id, String user2Id);
}
