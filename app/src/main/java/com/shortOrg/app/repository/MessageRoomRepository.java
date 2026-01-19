package com.shortOrg.app.repository;

import com.shortOrg.app.domain.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRoomRepository extends JpaRepository<MessageRoom,Long> {
    List<MessageRoom> findByUser1_IdOrUser2_Id(String user1Id, String user2Id);

    @Query("select r " +
            "from MessageRoom r " +
            "where r.id=:roomId " +
            "and (r.user1.id=:userId " +
            "or r.user2.id=:userId)")
    Optional<MessageRoom> findByIdAndUserId(@Param("roomId") Long id, @Param("userId") String userId);
}
