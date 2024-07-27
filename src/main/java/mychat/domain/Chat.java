package mychat.domain;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Chat")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat { // MongoDB에서 메시지 저장에 사용할 도메인 모델

    @Id
    private String id;
    @NotNull
    private String roomId;
    @NotNull
    private String username;
    @NotNull
    private String content;
    @NotNull
    private Long sendTime;
}
