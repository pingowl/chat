package mychat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mychat.domain.Book;
import mychat.service.ChatRoomService;
import mychat.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    // 채팅내역 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<Book> getChatHistoryAsBook(@PathVariable("roomId") String roomId) {
        chatRoomService.findRoomById(roomId);
        Book response = chatService.getChatHistoryAsBook(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> response = chatService.getAllBooks();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
