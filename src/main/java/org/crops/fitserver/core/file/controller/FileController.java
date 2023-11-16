package org.crops.fitserver.core.file.controller;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.core.file.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

  private final FileService fileService;

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> uploadFile(
      @RequestParam(value = "file") MultipartFile multipartFile) {
    var result = fileService.uploadFile(multipartFile.getOriginalFilename(), multipartFile, true);
    return ResponseEntity.ok(result);
  }
}
