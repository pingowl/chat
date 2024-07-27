package mychat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mychat.domain.Chat;
import mychat.service.ChatRoomService;
import mychat.service.ChatService;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.UUID;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations template;

    // MessageMapping 을 통해 webSocket 로 들어오는 메시지를 발신 처리한다.
    // 이때 클라이언트에서는 /publish/chat/message 로 요청하게 되고 이것을 controller 가 받아서 처리한다.
    // 처리가 완료되면 /subscribe/chat/room/roomId 로 메시지가 전송된다.
    @MessageMapping("chat/enterUser")
    public void enterUser(@Payload Chat chat, SimpMessageHeaderAccessor headerAccessor) {

        // 채팅방에 유저 추가
        chatRoomService.connectChatRoom(chat.getRoomId(), chat.getUsername());

        // 반환 결과를 socket session 에 userUUID 로 저장
        headerAccessor.getSessionAttributes().put("username", chat.getUsername());
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setContent("***  "+chat.getUsername() + " 님이 입장했습니다  ***");
        chat.setUsername("SYSTEM");
        chat.setId(UUID.randomUUID().toString());
        chat.setSendTime(System.currentTimeMillis());
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }

    @MessageMapping("/chat/sendMessage")
    public String sendMessage(@Payload Chat chat) {
        log.info("CHAT {}", chat);
        chat.setId(UUID.randomUUID().toString());
        String roomId = chat.getRoomId();
        template.convertAndSend("/sub/chat/room/" + roomId, chat);
        chatService.saveMessage(chat);
        if(chatRoomService.addChatCntAndReturn(roomId)){
            chatService.makeBook(roomId);
            return "fin";
        }
        return "";
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid 와 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        log.info("headerAccessor {}", headerAccessor);

        // 채팅방 유저 리스트에서 유저 닉네임 조회 및 리스트에서 유저 삭제
        if(chatRoomService.disconnectChatRoomAndReturnDone(roomId, username)) {
            chatService.makeBook(roomId);
        }
        log.info("User Disconnected : " + username);

        Chat chat = Chat.builder()
                .id(UUID.randomUUID().toString())
                .username("SYSTEM")
                .content("***  "+username + " 님이 퇴장했습니다  ***")
                .sendTime(System.currentTimeMillis())
                .build();
        template.convertAndSend("/sub/chat/room/" + roomId, chat);
    }

    @PostMapping("mongo/insert")
    public ResponseEntity<Void> mongoInsert(){
        chatService.mongoInsert();
        return new ResponseEntity(HttpStatus.OK);
    }
}
