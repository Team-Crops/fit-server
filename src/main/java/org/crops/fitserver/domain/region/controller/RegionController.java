package org.crops.fitserver.domain.region.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.region.dto.RegionDto;
import org.crops.fitserver.domain.region.dto.request.CreateRegionRequest;
import org.crops.fitserver.domain.region.dto.request.UpdateRegionRequest;
import org.crops.fitserver.domain.region.dto.response.GetRegionListResponse;
import org.crops.fitserver.domain.region.service.RegionService;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/region")
public class RegionController {

  private final RegionService regionService;

  @GetMapping()
  public ResponseEntity<GetRegionListResponse> getRegion() {
    return ResponseEntity.ok(GetRegionListResponse.of(regionService.getRegionList()));
  }

  @PostMapping()
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<RegionDto> createRegion(
      @Valid @RequestBody CreateRegionRequest createRegionRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(regionService.createRegion(createRegionRequest));
  }

  @PutMapping("/{regionId}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<RegionDto> updateRegion(@PathVariable Long regionId,
      @Valid @RequestBody UpdateRegionRequest updateRegionRequest) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(regionService.updateRegion(regionId, updateRegionRequest));
  }

  @DeleteMapping("/{regionId}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> deleteRegion(@PathVariable Long regionId) {
    regionService.deleteRegion(regionId);
    return ResponseEntity.ok().build();
  }
}
