package org.crops.fitserver.domain.school.controller;

import io.micrometer.common.util.StringUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.school.dto.SchoolDto;
import org.crops.fitserver.domain.school.service.SchoolService;
import org.crops.fitserver.global.annotation.V1;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@V1
@Slf4j
@Controller
@RequestMapping("/school")
@RequiredArgsConstructor
public class SchoolController {

  private final SchoolService schoolService;


  @GetMapping("")
  public ResponseEntity<List<SchoolDto>> getSchoolList(
      @RequestParam(required = false, name = "keyword") String keyword) {
    return ResponseEntity.ok(
        StringUtils.isBlank(keyword) ?
            schoolService.getSchoolList()
            : schoolService.getSchoolListByKeyword(keyword)
    );
  }
}
