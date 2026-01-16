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
        return ResponseEntity.ok(messageService.getRooms(auth.getName()));
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
