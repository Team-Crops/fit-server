package org.crops.fitserver.core.file.controller;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.core.file.dto.PreSignedUrlDto;
import org.crops.fitserver.core.file.dto.request.GeneratePreSignedUrlRequest;
import org.crops.fitserver.core.file.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

  private final FileService fileService;

  @PostMapping(value = "/pre-signed-url", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<PreSignedUrlDto> generatePreSignedUrl(
      @RequestBody GeneratePreSignedUrlRequest request) {
    var result = fileService.generatePreSignedUrl(request.fileName(), request.fileDomain());
    return ResponseEntity.ok(result);
  }
}
