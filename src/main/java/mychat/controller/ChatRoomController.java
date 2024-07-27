package mychat.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mychat.domain.Book;
import mychat.domain.ChatRoom;
import mychat.dto.response.IdResponse;
import mychat.service.ChatRoomService;
import mychat.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("chatrooms")
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @PostMapping("/create")
    public ResponseEntity<IdResponse> createChatRoom(@RequestParam(value="roomName") @NotBlank
                                                     @Size(min=1,max=10,message="방제목 길이는 1글자 이상 10이하여야 합니다.") String roomName,
                                                     @RequestParam(value="nickname") String nickname,
                                                     @RequestParam(required = false, defaultValue="5", value="maxUserCnt") Integer maxUserCnt){
        ChatRoom chatroom = chatRoomService.createChatRoom(roomName, nickname, maxUserCnt);
        IdResponse response = IdResponse.builder()
                .id(chatroom.getId())
                .build();
        log.info("CREATE Chat Room [{}]", chatroom);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoom> getChatRoomDetail(@PathVariable("roomId") String roomId){
        ChatRoom response = chatRoomService.findRoomById(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
