package org.crops.fitserver.domain.file.service;

import org.crops.fitserver.domain.file.constant.FileDomain;
import org.crops.fitserver.domain.file.dto.PreSignedUrlDto;

public interface FileService {

  PreSignedUrlDto generatePreSignedUrl(String fileName, FileDomain fileDomain);

  boolean isUploaded(String fileName);

  void deleteFile(String fileKey);
}
