package com.shortOrg.app.features.message;

import com.shortOrg.app.domain.*;
import com.shortOrg.app.repository.MessageRepository;
import com.shortOrg.app.repository.MessageRoomRepository;
import com.shortOrg.app.repository.RoomReadStateRepository;
import com.shortOrg.app.features.message.dto.EnsureRoomAndSendMessageRequest;
import com.shortOrg.app.features.message.dto.MessageResponse;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSendService {
    private final MessageRoomRepository messageRoomRepository;
    private final EntityManager entityManager;
    private final MessageRepository messageRepository;
    private final RoomReadStateRepository roomReadStateRepository;

    @Transactional
    public MessageResponse sendRoomMessage(Long roomId, String content, String senderId) {
        //채팅방 존재 확인+채팅방 내에 요청 유저가 있는지 확인
        Optional<MessageRoom> optionalRoom = messageRoomRepository.findByIdAndUserId(roomId, senderId);
        if(optionalRoom.isEmpty())
            throw new IllegalArgumentException("해당 id의 채팅방이 존재하지 않거나 요청 유저가 해당 채팅방에 속해있지 않음");

        return sendMessage(optionalRoom.get(), content, senderId);
    }

    @Transactional
    public MessageResponse ensureRoomAndSendMessage(EnsureRoomAndSendMessageRequest request, String senderId) {
        //유저 순서 판별
        String receiverId = request.getReceiverId();
        int compareResult = senderId.compareTo(receiverId);
        String user1Id = null;
        String user2Id = null;
        if(compareResult < 0) {
            user1Id = senderId;
            user2Id = receiverId;
        }
        else if(compareResult > 0) {
            user1Id = receiverId;
            user2Id = senderId;
        }
        else {
            throw new IllegalArgumentException("자기자신에게 메시지를 보낼 수 없음");
        }

        //채팅방 존재 확인
        var optionalMessageRoom = messageRoomRepository.findByPostIdAndUserIds(request.getPostId(), user1Id, user2Id);

        MessageRoom messageRoom = null;
        if (optionalMessageRoom.isEmpty()) { //없으면 생성
            User sender = entityManager.getReference(User.class, senderId);
            User receiver = entityManager.getReference(User.class, request.getReceiverId());
            Post post = entityManager.getReference(Post.class, request.getPostId());

            try {
                messageRoom = messageRoomRepository.saveAndFlush(new MessageRoom(sender, receiver, post));
            }
            catch (DataIntegrityViolationException e) { //생성시도가 동시에 일어나서 예외가 발생한 경우
                entityManager.clear();
                messageRoom = messageRoomRepository.findByPostIdAndUserIds(request.getPostId(), user1Id, user2Id) //DB에서 다시 한번 조회 시도
                        .orElseThrow(()->e); //조회 실패시(=동시 생성시도가 아닌 다른 문제의 경우) 기존 예외 다시 throw
            }
        }
        else { //있으면 그냥 사용
            messageRoom = optionalMessageRoom.get();
        }
        return sendMessage(messageRoom, request.getContent(), senderId);
    }

    //메시지 전송
    private MessageResponse sendMessage(MessageRoom messageRoom, String content, String senderId) {
        //메시지 전송
        User sender = entityManager.getReference(User.class, senderId);
        User receiver = messageRoom.getOpponent(senderId).orElseThrow(()->new IllegalStateException("채팅 상대를 찾을 수 없음"));
        Message message = new Message(messageRoom, messageRoom.getPost(), sender, receiver, content);
        Message savedMessage = messageRepository.saveAndFlush(message);

        //최근 메시지 갱신
        messageRoom.setLastMessage(savedMessage);
        messageRoomRepository.save(messageRoom);

        //유저 읽기 상태 갱신(자신이 방금 보낸 메시지까지 모두 읽었다고 표시)
        Optional<RoomReadState> optionalReadState = roomReadStateRepository.findByMessageRoom_IdAndUser_Id(messageRoom.getId(), senderId);
        RoomReadState readState = null;
        if(optionalReadState.isPresent()) {
            readState = optionalReadState.get();
            if(readState.getLastReadMessage().getId() < savedMessage.getId()) //마지막으로 읽은 메시지가 현재 메시지보다 더 과거의 메시지일 경우에만 update
                readState.setLastReadMessage(savedMessage);
            roomReadStateRepository.save(readState);
        }
        else {
            readState = new RoomReadState();
            readState.setMessageRoom(messageRoom);
            readState.setUser(entityManager.getReference(User.class,senderId));
            readState.setLastReadMessage(savedMessage);
            try {
                roomReadStateRepository.save(readState);
            } catch (DataIntegrityViolationException e) {
                entityManager.clear();
                readState = roomReadStateRepository.findByMessageRoom_IdAndUser_Id(messageRoom.getId(), senderId)
                        .orElseThrow(()->e);
                readState.setLastReadMessage(savedMessage);
            }
        }

        //전송한 메시지 반환
        return new MessageResponse(
                message.getId(),
                message.getMessageRoom().getId(),
                message.getPost().getId(),
                message.getSender().getId(),
                message.getContent(),
                message.getPost().getTitle()
        );
    }
}
