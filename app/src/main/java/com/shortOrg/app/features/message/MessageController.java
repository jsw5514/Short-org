package com.shortOrg.app.features.message;

import com.shortOrg.app.shared.dto.EnsureRoomAndSendMessageRequest;
import com.shortOrg.app.shared.dto.ErrorResponse;
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
    public ResponseEntity<?> getRoomMessage(@PathVariable Long roomId, Authentication auth) {
        try {
            return ResponseEntity.ok(messageService.getRoomMessage(roomId, auth.getName()));
        }
        catch (IllegalStateException e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("INTERNAL_SERVER_ERROR","서버 내부 오류 발생. 관리자에게 문의하세요."));
        }
    }
    
    @PostMapping("/room/{roomId}")
    public ResponseEntity<?> sendRoomMessage(@PathVariable Long roomId, @RequestBody String content, Authentication auth) {
        try {
            return ResponseEntity.ok(messageService.sendRoomMessage(roomId, content, auth.getName()));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("BAD_REQUEST","잘못된 파라미터 값입니다.")
            );
        }
    }
    
    @PostMapping("")
    public ResponseEntity<?> ensureRoomAndSendMessage(@RequestBody EnsureRoomAndSendMessageRequest request, Authentication auth) {
        try {
            return ResponseEntity.ok(messageService.ensureRoomAndSendMessage(request, auth.getName()));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("자기 자신에게 메시지를 보낼 수 없음");
        }
    }
}
