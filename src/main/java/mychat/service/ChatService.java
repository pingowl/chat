package mychat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mychat.common.exception.CustomException;
import mychat.common.exception.ErrorCode;
import mychat.domain.Book;
import mychat.domain.ChatRoom;
import mychat.repository.BookRepository;
import mychat.repository.ChatRepository;
import mychat.repository.ChatRoomRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import mychat.domain.Chat;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MongoTemplate mongoTemplate;
    private final BookRepository bookRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void saveMessage(Chat chat) {
//        chatSender.send(ConstantUtil.KAFKA_TOPIC, chat);
        chatRepository.save(chat);
    }

    public Book getChatHistoryAsBook(String roomId){
        Book book = bookRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));
        return book;
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public void makeBook(String roomId){
        List<Chat> chats = chatRepository.findByRoomId(roomId);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
        StringBuilder sb = new StringBuilder();
        for(Chat c: chats) sb.append(c.getContent());
        Instant instance = Instant.ofEpochMilli(chats.get(0).getSendTime());
        Book book = Book.builder()
                .id(roomId)
                .roomName(chatRoom.getRoomName())
                .content(sb.toString())
                .createdAt(LocalDateTime.ofInstant(instance, ZoneId.of("Asia/Seoul")))
                .build();
        bookRepository.save(book);
    }

    public void mongoInsert(){
        Chat chat = new Chat("chat1", "1", "Lee", "내용",
                System.currentTimeMillis());
        mongoTemplate.insert(chat);
    }
}
