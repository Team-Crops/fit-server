package org.crops.fitserver.domain.skillset.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.skillset.dto.PositionDto;
import org.crops.fitserver.domain.skillset.dto.SkillDto;
import org.crops.fitserver.domain.skillset.dto.request.AddSkillListToPositionRequest;
import org.crops.fitserver.domain.skillset.dto.request.AddSkillToPositionListRequest;
import org.crops.fitserver.domain.skillset.dto.request.CreatePositionRequest;
import org.crops.fitserver.domain.skillset.dto.request.CreateSkillRequest;
import org.crops.fitserver.domain.skillset.dto.request.UpdatePositionRequest;
import org.crops.fitserver.domain.skillset.dto.request.UpdateSkillRequest;
import org.crops.fitserver.domain.skillset.dto.response.GetPositionListResponse;
import org.crops.fitserver.domain.skillset.dto.response.GetSkillListResponse;
import org.crops.fitserver.domain.skillset.service.SkillSetService;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequestMapping("/skill-set")
@RequiredArgsConstructor
public class SkillSetController {

  private final SkillSetService skillSetService;

  @GetMapping("/position")
  public ResponseEntity<GetPositionListResponse> getPositionList() {
    return ResponseEntity.ok(GetPositionListResponse.of(skillSetService.getPositionList()));
  }

  @PostMapping("/position")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<PositionDto> createPosition(
      @Valid @RequestBody CreatePositionRequest createPositionRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(skillSetService.createPosition(createPositionRequest));
  }

  @PatchMapping("/position/{positionId}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<PositionDto> updatePosition(@PathVariable Long positionId,
      @Valid @RequestBody UpdatePositionRequest updatePositionRequest) {
    return ResponseEntity.status(HttpStatus.OK).body(
        skillSetService.updatePositionDisplayName(positionId, updatePositionRequest.displayName().get())
    );
  }

  @DeleteMapping("/position/{positionId}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> deletePosition(@PathVariable Long positionId) {
    skillSetService.deletePosition(positionId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/position/{positionId}/skill")
  public ResponseEntity<GetSkillListResponse> getSkillListByPositionId(@PathVariable Long positionId) {
    return ResponseEntity.ok(GetSkillListResponse.of(skillSetService.getSkillListByPositionId(positionId)));
  }

  @PatchMapping("/position/{positionId}/skill")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<PositionDto> addSkillToPosition(@PathVariable Long positionId,
      @Valid @RequestBody
      AddSkillListToPositionRequest addSkillListToPositionRequest) {
    return ResponseEntity.status(HttpStatus.OK).body(
        skillSetService.addSkillListToPosition(positionId,
            addSkillListToPositionRequest.skillIds()));
  }

  @GetMapping("/skill")
  public ResponseEntity<GetSkillListResponse> getSkillList() {
    return ResponseEntity.ok(GetSkillListResponse.of(skillSetService.getSkillList()));
  }

  @PostMapping("/skill")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<SkillDto> createSkill(
      @Valid @RequestBody CreateSkillRequest createSkillRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(skillSetService.createSkill(createSkillRequest));
  }

  @PatchMapping("/skill/{skillId}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<SkillDto> updateSkill(@PathVariable Long skillId,
      @Valid @RequestBody UpdateSkillRequest updateSkillRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(skillSetService.updateSkillDisplayName(skillId, updateSkillRequest.displayName().get()));
  }

  @DeleteMapping("/skill/{skillId}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> deleteSkill(@PathVariable Long skillId) {
    skillSetService.deleteSkill(skillId);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/skill/{skillId}/position")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<SkillDto> addSkillToPositionList(@PathVariable Long skillId,
      @Valid @RequestBody AddSkillToPositionListRequest addSkillToPositionListRequest) {
    return ResponseEntity.status(HttpStatus.OK).body(
        skillSetService.addSkillToPositionList(skillId,
            addSkillToPositionListRequest.positionIds()));
  }
}
