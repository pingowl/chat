package mychat.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // User 예외
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    EXIST_USER_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    SECURITY_CONTEXT_NOT_FOUND(HttpStatus.NOT_FOUND, "Security Context 에 인증 정보가 없습니다."),
    EXIST_USER(HttpStatus.CONFLICT, "이미 가입되어 있는 유저입니다."),
    WRONG_PASSWORD(HttpStatus.CONFLICT, "잘못된 비밀번호입니다."),
    LOGOUTED_USER(HttpStatus.CONFLICT, "로그아웃 된 사용자입니다."),
    LOGIN_ERROR(HttpStatus.CONFLICT, "로그인 에러"),

    // ChatRoom 예외
    NOT_FOUND_CHATROOM(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다."),
    CANNOT_ENTER_CHATROOM(HttpStatus.CONFLICT, "해당 채팅방의 인원이 가득차서 입장할 수 없습니다."),
    ALREADY_IN_CHATROOM(HttpStatus.CONFLICT, "이미 해당 채팅방의 인원입니다."),
    NOT_MEMBER_CHATROOM(HttpStatus.NOT_FOUND, "해당 채팅방의 인원이 아닙니다."),

    // Book 예외
    NOT_FOUND_BOOK(HttpStatus.NOT_FOUND, "해당 소설책을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}