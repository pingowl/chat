package mychat.repository;

import mychat.domain.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    void deleteById(String roomId);
    @Query("{ 'done' : ?0 }")
    List<ChatRoom> findByDone(boolean d);
}
