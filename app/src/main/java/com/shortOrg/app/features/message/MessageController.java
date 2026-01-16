package com.shortOrg.app.features.message;

import com.shortOrg.app.shared.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

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
    public ResponseEntity<?> getRoomMessage(@PathVariable Long roomId, Authentication auth) {
        try {
            return ResponseEntity.ok(messageService.getRoomMessage(roomId, auth.getName()));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("INTERNAL_SERVER_ERROR","서버 내부 오류 발생. 관리자에게 문의하세요."));
        }
    }
    
    @PostMapping("/room/{roomId}")
    public ResponseEntity<?> sendRoomMessage(@PathVariable String roomId, @RequestBody String message) {
        //TODO 메시지 전송, 내부적으로 가장 최근 메시지 갱신
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);//TODO 구현필요
    }
    
    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody String message) {
        //TODO 메시지 전송, 내부적으로 가장 최근 메시지 갱신
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);//TODO 구현필요
    }
}
