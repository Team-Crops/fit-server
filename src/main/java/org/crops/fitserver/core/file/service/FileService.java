package org.crops.fitserver.core.file.service;

import org.crops.fitserver.core.file.constant.FileDomain;
import org.crops.fitserver.core.file.dto.PreSignedUrlDto;

public interface FileService {

  PreSignedUrlDto generatePreSignedUrl(String fileName, FileDomain fileDomain);

  void deleteFile(String fileKey);
}
