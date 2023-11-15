package org.crops.fitserver.core.file;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  //fileKey is the newly created name of the file
  public String uploadFile(String fileName, MultipartFile file, boolean isTemporary)
      throws IOException;

  public boolean deleteFile(String fileKey);
}
