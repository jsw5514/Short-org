package com.shortOrg.app.repository;

import com.shortOrg.app.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    long countByMessageRoom_Id(Long messageRoomId);

    long countByMessageRoom_IdAndIdAfter(Long messageRoomId, Long idAfter);

    List<Message> findByMessageRoom_IdOrderByIdDesc(Long messageRoomId);

    List<Message> findByMessageRoom_IdAndIdIsGreaterThanOrderByIdDesc(Long messageRoomId, Long idIsGreaterThan);
}
