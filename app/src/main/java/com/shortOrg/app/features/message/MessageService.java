package com.shortOrg.app.features.message;

import com.shortOrg.app.domain.MessageRoom;
import com.shortOrg.app.repository.MessageRepository;
import com.shortOrg.app.repository.MessageRoomRepository;
import com.shortOrg.app.shared.dto.MessageRoomResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;

    @Transactional
    public MessageRoomResponse getRooms(String userId) {
        //TODO 채팅방 데이터 구조 구축 후 구현할것(유저 id로 그 유저가 가입한 채팅방 목록을 검색)
        List<MessageRoom> roomList = messageRoomRepository.findByUser1_IdOrUser2_Id(userId, userId);
//        roomList.stream()
                
        throw new RuntimeException("Not Implemented");
    }
}
