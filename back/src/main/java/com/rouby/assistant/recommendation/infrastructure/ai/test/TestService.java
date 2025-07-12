package com.rouby.assistant.recommendation.infrastructure.ai.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.assistant.recommendation.infrastructure.ai.test.dto.TestRequest;
import com.rouby.assistant.recommendation.infrastructure.ai.test.dto.TestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.stereotype.Service;

/**
 * @Date : 2025. 07. 11.
 *
 * @author : hanjihoon
 */
@Service
@Slf4j
public class TestService {

  private final ChatClient chatClient;

  public TestService(ChatModel chatModel) {
    this.chatClient = ChatClient.builder(chatModel).build();
  }

  private final ObjectMapper objectMapper = new ObjectMapper();

  public TestResponse aiTest(TestRequest request){
//    String prompt = generatePrompt(request); // 프롬프트 생성
    String prompt = generatePromptEx(request); // 프롬프트 생성

    ChatResponse chatResponse = chatClient.prompt(prompt)
        .call() // ChatModel 호출
        .chatResponse(); // 결과 받아오기

    if (chatResponse == null) {
      throw new RuntimeException("Chat response is null");
    }

    Generation result = chatResponse.getResult(); // 첫 번째 응답 generation
    AssistantMessage output = result.getOutput(); // 메시지 출력값 추출
    String text = output.getText(); // 텍스트 내용 추출


    log.info(text);
    return parseResult(text); // JSON 파싱
  }

  private String generatePrompt(TestRequest request) {
    return String.format("""
                 Task: Extract time from the text and return a JSON response.
 
                 - Identify time and convert it to ISO 8601 (YYYY-MM-DDTHH:MM:SS).
                 - Remove the identified time from the text. The remaining text becomes `content`.
                 - If no time is found, return:
                   { "result": true, "hasTime": false}
                 - If multiple time exists, return:
                   { "result": false }
 
                 Respond in JSON format only, with the following fields:
                 - result
                 - hasTime
                 - datetime
                 - content
 
                 No explanations.
 
                 ===
 
                 input:
 
                 {
                     "date": "%s",
                     "content": "%s"
                 }
                """, request.date(), request.content());
  }

  private String generatePromptEx(TestRequest request) {
    return String.format("""
                Task: Analyze the given content and return your opinion in a JSON response.
                
                - Read the content and think critically.
                - Respond with your own brief opinion or insight on the content.
                - If the content is vague or unclear, return:
                  { "result": false }
                
                Respond in JSON format only, with the following fields:
                - result
                - content
                
                No explanations. Only respond with a JSON object.
                
                ===
                
                input:
                
                {
                    "content": "%s"
                }
                """, request.content());

  }

  private TestResponse parseResult(String text) {
    // 텍스트 중 코드블록 문법 제거 (```json ... ```)
    String jsonText = text.lines()
        .filter(line -> !line.startsWith("```")) // 코드 블록 제거
        .reduce("", (a, b) -> a + b); // 문자열 합치기
    try {
      return objectMapper.readValue(jsonText, TestResponse.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
