package mychat.controller;

import mychat.dto.request.GPTRequest;
import mychat.dto.response.GPTResponse;
import lombok.RequiredArgsConstructor;
import mychat.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("gpt")
@RequiredArgsConstructor
public class GPTController {

    private final ChatRoomService chatRoomService;

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;


    @GetMapping("/chat/{roomId}")
    public String chat(@PathVariable("roomId") String roomId){
        String prompt = "릴레이로 소설 작성할건데 소설의 첫 줄 써줄래? 20자 이내로";

        GPTRequest request = new GPTRequest(
                model,prompt,1,256,1,2,2);

        GPTResponse gptResponse = restTemplate.postForObject(
                apiUrl
                , request
                , GPTResponse.class
        );

        String firstSentence = gptResponse.getChoices().get(0).getMessage().getContent();
        // 입력 문자열을 10글자씩 잘라서 줄바꿈 추가
        StringBuilder sb = new StringBuilder();
        int length = firstSentence.length();
        int chunkSize = 10; // 줄바꿈을 추가할 기준 길이
        for (int i = 0; i < length; i += chunkSize) {
            // 현재 위치에서 chunkSize만큼 잘라서 추가
            int end = Math.min(length, i + chunkSize);
            sb.append(firstSentence, i, end);
            sb.append('\n'); // 줄바꿈 추가
        }
        firstSentence = sb.toString();

        chatRoomService.setFirstSentence(roomId, firstSentence);
        return firstSentence;

    }
}
