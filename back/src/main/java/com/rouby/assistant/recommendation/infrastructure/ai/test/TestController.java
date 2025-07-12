package com.rouby.assistant.recommendation.infrastructure.ai.test;

import com.rouby.assistant.recommendation.infrastructure.ai.test.dto.TestRequest;
import com.rouby.assistant.recommendation.infrastructure.ai.test.dto.TestResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date : 2025. 07. 11.
 *
 * @author : hanjihoon
 */
@RestController
@RequestMapping("/test")
@Slf4j
@RequiredArgsConstructor
public class TestController {

  private final TestService testService;

  @GetMapping("/")
  public TestResponse test(
      @RequestParam int year,
      @RequestParam int month,
      @RequestParam int day,
      @RequestParam String content
  ){
    LocalDate date = LocalDate.of(year, month, day);
    TestRequest request = new TestRequest(date, content);
    log.info("요청");
    TestResponse testResponse = testService.aiTest(request);
    log.info(testResponse.content(), "응답");
    return testResponse;
  }
}
