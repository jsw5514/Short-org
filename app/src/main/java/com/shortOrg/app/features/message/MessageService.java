package com.shortOrg.app.features.message;

import com.shortOrg.app.domain.MessageRoom;
import com.shortOrg.app.domain.RoomReadState;
import com.shortOrg.app.repository.MessageRepository;
import com.shortOrg.app.repository.MessageRoomRepository;
import com.shortOrg.app.repository.RoomReadStateRepository;
import com.shortOrg.app.shared.dto.MessageRoomResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final RoomReadStateRepository roomReadStateRepository;

    @Transactional
    public List<MessageRoomResponse> getRooms(String userId) {
        List<MessageRoom> roomList = messageRoomRepository.findByUser1_IdOrUser2_Id(userId, userId);
        
        ArrayList<MessageRoomResponse> messageRoomResponses = new ArrayList<>();
        //채팅방목록 -> dto로 변환 -> 안 읽은 메시지 갯수 추가
        for(MessageRoom room : roomList) {
            MessageRoomResponse roomResponse = new MessageRoomResponse(
                    room.getId(),
                    room.getUser1().getId().equals(userId) ? room.getUser2().getId() : room.getUser1().getId(),
                    room.getPost().getId(),
                    room.getLastMessage().getContent()
            );
            roomResponse.setNotReadCount(countNotRead(room.getId(), userId));
            messageRoomResponses.add(roomResponse);
        }
        return messageRoomResponses;
    }
    
    //TODO 리포지토리 메소드가 JPQL을 이용하면 쿼리 횟수 2->1로 최적화할 여지 있음
    //유저가 채팅방에서 읽지 않은 채팅 갯수를 반환
    private long countNotRead(long roomId, String userId) {
        Optional<RoomReadState> readState = roomReadStateRepository.findByMessageRoom_IdAndUser_Id(roomId, userId); //해당 유저가 읽은 적이 있는지 확인
        if(readState.isPresent()) { //읽은 적이 있다면
            return messageRepository.countByMessageRoom_IdAndIdAfter(roomId, readState.get().getLastReadMessage().getId()); //마지막으로 읽은 메시지 이후 것만 세서 반환
        }
        else { //읽은 적이 없다면
            return messageRepository.countByMessageRoom_Id(roomId); //채팅방의 전체 메시지를 세서 반환
        }
    }
}
