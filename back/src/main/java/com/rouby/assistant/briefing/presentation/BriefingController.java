package com.rouby.assistant.briefing.presentation;

import com.rouby.assistant.briefing.application.facade.BriefingFacade;
import com.rouby.assistant.briefing.presentation.dto.response.BriefingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 07.
 */
@RestController
@RequestMapping("/api/v1/assistants/briefings")
@RequiredArgsConstructor
public class BriefingController {

  private final BriefingFacade briefingFacade;

  @GetMapping("/{id}")
  public ResponseEntity<BriefingResponse> getBriefing(
      @PathVariable Long id) {
    return ResponseEntity.ok().body(BriefingResponse.of(briefingFacade.getBriefing(id)));
  }


}
