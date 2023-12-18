package org.crops.fitserver.core.file.service;

import static org.crops.fitserver.global.exception.ErrorCode.FILE_UPLOAD_EXCEPTION;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3FileServiceImpl implements FileService {

  private final S3Client s3Client;

  @Value("${aws.s3.bucket}")
  private String bucket;
  private final static String FILE_PREFIX = "file/";
  private final static String TEMPORARY_FILE_PREFIX = "temp/";


  @Override
  public String uploadFile(String fileName, MultipartFile file, boolean isTemporary) {
    var fileKey = createFileKey(fileName, isTemporary);

    try {
      s3Client.putObject(
          PutObjectRequest.builder().bucket(bucket).key(fileKey)
              .build(),
          RequestBody.fromBytes(file.getBytes())
      );
    } catch (IOException e) {
      throw new BusinessException(FILE_UPLOAD_EXCEPTION);
    }

    return fileKey;
  }

  @Override
  public void deleteFile(String fileKey) {
    s3Client.deleteObject(builder -> builder.bucket(bucket).key(fileKey));
  }

  private static String createFileKey(String fileName, boolean isTemporary) {
    return (isTemporary ? TEMPORARY_FILE_PREFIX : FILE_PREFIX)+ UUID.randomUUID() + fileName;
  }
}
