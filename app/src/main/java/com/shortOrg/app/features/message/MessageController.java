package com.shortOrg.app.features.message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/room")
    public ResponseEntity<?> getRoomList(Authentication auth) {
        messageService.getRooms(auth.getName());
        //TODO getRooms 결과를 리턴해야함(예외처리는 별도)
        //채팅방 목록 확인
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
    
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getRoomMessage(@PathVariable String roomId) {
        //채팅방 내 채팅 확인
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);//TODO 구현필요
    }
    
    @PostMapping("/room/{roomId}")
    public ResponseEntity<?> sendRoomMessage(@PathVariable String roomId, @RequestBody String message) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);//TODO 구현필요
    }
    
    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody String message) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);//TODO 구현필요
    }
}
