package mychat.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.ArrayList;
import java.util.List;

@Document(collection = "ChatRoom")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @NotNull
    private String id; // 채팅방 아이디
    private String roomName; // 채팅방 이름
    private String headNickname; // 채팅방 방장
    @Builder.Default
    private int userCount=0; // 채팅방 인원수
    private int maxUserCnt; // 채팅방 최대 인원 제한
    @Builder.Default
    private boolean done=false;
    private String firstSentence;

    // 채팅방 삭제시 방장이 비밀번호 입력해야함

    // Val:nickname
    @Builder.Default
    public List<String> participants = new ArrayList<>();

    public boolean addParticipant(String username){
        if(userCount >= maxUserCnt) return false;
        participants.add(username);
        userCount++;
        return true;
    }

    public boolean deleteParticipant(String username){
        if(!participants.contains(username)) return false;
        participants.remove(username);
        userCount--;
        if(userCount==0) done=true;
        return true;
    }

    public boolean existsMember(String nickname){
        if(participants.contains(nickname)) return true;
        return false;
    }

    public void setAsDone(){
        done = true;
    }

    public void setFirstSentence(String firstSentence){
        this.firstSentence = firstSentence;
    }
}
