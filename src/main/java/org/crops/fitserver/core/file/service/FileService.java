package org.crops.fitserver.core.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  //fileKey is the newly created name of the file
  String uploadFile(String fileName, MultipartFile file, boolean isTemporary);

  boolean deleteFile(String fileKey);
}
