package mychat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mychat.repository.ChatRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import mychat.domain.Chat;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MongoTemplate mongoTemplate;

    public void saveMessage(Chat chat) {
        chatRepository.save(chat);
    }

    public List<Chat> getChatHistory(String roomId){
        return chatRepository.findByRoomId(roomId);
    }

    public void mongoInsert(){
        Chat chat = new Chat("chat1", "1", "Lee", "내용",
                System.currentTimeMillis());
        mongoTemplate.insert(chat);
    }
}

