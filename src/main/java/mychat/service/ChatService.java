package mychat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mychat.repository.ChatRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import mychat.domain.Chat;
import org.springframework.transaction.annotation.Transactional;



@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MongoTemplate mongoTemplate;

}
