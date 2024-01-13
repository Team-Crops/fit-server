package org.crops.fitserver.domain.skillset.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.skillset.dto.PositionDto;
import org.crops.fitserver.domain.skillset.service.SkillSetService;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@V1
@Slf4j
@Controller
@RequestMapping("/skill-set")
@RequiredArgsConstructor
public class SkillSetController {
  private final SkillSetService skillSetService;

  @GetMapping("/position")
  public ResponseEntity<List<PositionDto>> getPositionList() {
    return ResponseEntity.ok(skillSetService.getPositionList());
  }
}
