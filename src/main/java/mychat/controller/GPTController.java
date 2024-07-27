package mychat.controller;

import mychat.dto.request.GPTRequest;
import mychat.dto.response.GPTResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("gpt")
@RequiredArgsConstructor
public class GPTController {

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;


    @GetMapping("/chat")
    public String chat(){
        String prompt = "릴레이로 소설 작성할건데 소설의 첫 줄 써줄래?";

        GPTRequest request = new GPTRequest(
                model,prompt,1,256,1,2,2);

        GPTResponse gptResponse = restTemplate.postForObject(
                apiUrl
                , request
                , GPTResponse.class
        );


        return gptResponse.getChoices().get(0).getMessage().getContent();


    }
}
