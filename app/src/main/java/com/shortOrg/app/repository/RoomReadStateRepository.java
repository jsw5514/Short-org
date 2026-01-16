package com.shortOrg.app.repository;

import com.shortOrg.app.domain.RoomReadState;
import com.shortOrg.app.domain.embeddedId.RoomReadStateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomReadStateRepository extends JpaRepository<RoomReadState, RoomReadStateId> {
    Optional<RoomReadState> findByMessageRoom_IdAndUser_Id(Long messageRoom_id, String user_id);
}
