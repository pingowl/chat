package mychat.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Book")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    private String id; // roomId 와 동일
    @NotNull
    private String roomName;
    @NotNull
    private String content;
    @NotNull
    private LocalDateTime createdAt;
}
