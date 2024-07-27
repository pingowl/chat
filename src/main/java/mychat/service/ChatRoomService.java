package mychat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mychat.common.exception.CustomException;
import mychat.common.exception.ErrorCode;
import mychat.domain.ChatRoom;
import mychat.repository.ChatRoomRepository;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessageSendingOperations template;

    public ChatRoom createChatRoom(String roomName, String username, Integer maxUserCnt){
        ChatRoom chatRoom = ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .roomName(roomName)
                .headNickname(username)
                .maxUserCnt(maxUserCnt)
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
    }

    public void connectChatRoom(String roomId, String username) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        if(room.existsMember(username)) throw new CustomException(ErrorCode.ALREADY_IN_CHATROOM);
        if(!room.addParticipant(username)) throw new CustomException(ErrorCode.CANNOT_ENTER_CHATROOM);
        chatRoomRepository.save(room);

        log.info("[채팅방 입장] room={}, user={}", room.getId(), username);
    }

    public boolean disconnectChatRoomAndReturnDone(String roomId, String username) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
        room.deleteParticipant(username);
        chatRoomRepository.save(room);
        log.info("[채팅방 퇴장] room={}, user={}", room.getId(), username);
        if(room.getUserCount()==0)  return true;
        return false;
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    private void deleteChatRoom(String roomId){
        chatRoomRepository.deleteById(roomId);
    }

    public void setFirstSentence(String roomId, String firstSentence){
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
        room.setFirstSentence(firstSentence);
    }

    public boolean addChatCntAndReturn(String roomId){
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
        int cnt = room.addChatCnt();
        chatRoomRepository.save(room);
        if(cnt >= room.getUserCount()){
            room.setAsDone();
            return true;
        }
        return false;
    }
}
