package org.crops.fitserver.core.file;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
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

  @Value("${aws.s3.bucketNameForPermanent}")
  private String bucketNameForPermanent;

  @Value("${aws.s3.bucketNameForTemporary}")
  private String bucketNameForTemporary;

  @Override
  public String uploadFile(String fileName, MultipartFile file, boolean isTemporary)
      throws IOException {
    var bucketName = isTemporary ? bucketNameForTemporary : bucketNameForPermanent;

    var putObjectResponse = s3Client.putObject(
        PutObjectRequest.builder().bucket(bucketName).key(fileName)
            .build(),
        RequestBody.fromBytes(file.getBytes())
    );

    System.out.println(putObjectResponse.toString());

    return fileName;//TODO: create unique file name(filename+timestamp+random number)
  }

  @Override
  public boolean deleteFile(String fileKey) {
    return false;//TODO: implement
  }
}
