package org.crops.fitserver.domain.school.controller;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.school.dto.response.GetSchoolListResponse;
import org.crops.fitserver.domain.school.service.SchoolService;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@V1
@Slf4j
@RestController
@RequestMapping("/school")
@RequiredArgsConstructor
public class SchoolController {

  private final SchoolService schoolService;


  @GetMapping("")
  public ResponseEntity<GetSchoolListResponse> getSchoolList(
      @RequestParam(required = false, name = "keyword") String keyword) {
    return ResponseEntity.ok(
        GetSchoolListResponse.of(
            StringUtils.isBlank(keyword) ?
                schoolService.getSchoolList()
                : schoolService.getSchoolListByKeyword(keyword)
        )
    );
  }
}
