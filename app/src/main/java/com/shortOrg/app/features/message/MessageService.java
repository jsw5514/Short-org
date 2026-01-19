package com.shortOrg.app.features.message;

import com.shortOrg.app.domain.*;
import com.shortOrg.app.repository.MessageRepository;
import com.shortOrg.app.repository.MessageRoomRepository;
import com.shortOrg.app.repository.RoomReadStateRepository;
import com.shortOrg.app.shared.dto.EnsureRoomAndSendMessageRequest;
import com.shortOrg.app.shared.dto.MessageResponse;
import com.shortOrg.app.shared.dto.MessageRoomResponse;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final RoomReadStateRepository roomReadStateRepository;
    private final EntityManager entityManager;

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

    @Transactional
    public List<MessageResponse> getRoomMessage(Long roomId, String userId) {
        //해당 사용자가 마지막으로 확인한 메시지 확인
        Optional<RoomReadState> readState = roomReadStateRepository.findByMessageRoom_IdAndUser_Id(roomId, userId);
        
        List<Message> messages = null;
        RoomReadState recentReadState = null;
        //있으면 그 메시지 이후(id가 더 큰 쪽)만 리턴
        if(readState.isPresent()) {
            recentReadState = readState.get();
            messages = messageRepository.findByMessageRoom_IdAndIdIsGreaterThanOrderByIdDesc(roomId, recentReadState.getLastReadMessage().getId());
        }
        //없으면 채팅방 모든 메시지 리턴
        else {
            messages = messageRepository.findByMessageRoom_IdOrderByIdDesc(roomId);

            if (messages.isEmpty()) {//채팅방은 첫 채팅과 함께 생성되기에 비어있을 수 없음
                log.error("비어있는 채팅방이 발견됨. 채팅방 id: {}",roomId);
                throw new IllegalStateException("비어있는 채팅방 발견");
            }
            recentReadState = new RoomReadState();
            recentReadState.setMessageRoom(entityManager.getReference(MessageRoom.class, roomId));
            recentReadState.setUser(entityManager.getReference(User.class, userId));
        }
        //마지막으로 확인한 메시지 갱신
        recentReadState.setLastReadMessage(messages.getFirst());
        roomReadStateRepository.save(recentReadState);
        return messages.stream().map(message -> new MessageResponse(
                message.getId(),
                message.getMessageRoom().getId(),
                message.getPost().getId(),
                message.getSender().getId(),
                message.getContent()
        )).toList();
    }

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

        //전송한 메시지 반환
        return new MessageResponse(
                message.getId(),
                message.getMessageRoom().getId(),
                message.getPost().getId(),
                message.getSender().getId(),
                message.getContent()
        );
    }
}
