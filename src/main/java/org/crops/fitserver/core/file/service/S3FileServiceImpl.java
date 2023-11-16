package org.crops.fitserver.core.file.service;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@RequiredArgsConstructor
public class S3FileServiceImpl implements FileService {

  private final S3Client s3Client;

  @Value("${aws.s3.bucket}")
  private String bucket;


  @Override
  public String uploadFile(String fileName, MultipartFile file, boolean isTemporary) {
    fileName = isTemporary ? "temp/" + fileName : "file/" + fileName; // TODO: use enum

    try {
      s3Client.putObject(
          PutObjectRequest.builder().bucket(bucket).key(fileName)
              .build(),
          RequestBody.fromBytes(file.getBytes())
      );
    } catch (IOException e) {
      throw new RuntimeException(e);// TODO: 롤백할 수 있게 사용자 에러로 수정
    }

    return fileName;//TODO: create unique file name(filename+timestamp+random number)
  }

  @Override
  public boolean deleteFile(String fileKey) {
    return false;//TODO: implement
  }
}
