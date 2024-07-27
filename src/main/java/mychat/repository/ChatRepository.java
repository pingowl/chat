package mychat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import mychat.domain.Chat;
import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    @Query("{ 'roomId' : ?0}")
    List<Chat> findByRoomId(String room);
}