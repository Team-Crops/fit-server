package org.crops.fitserver.domain.file.service;

import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.file.constant.FileDomain;
import org.crops.fitserver.domain.file.dto.PreSignedUrlDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3FileServiceImpl implements FileService {

  private final S3Client s3Client;

  private final S3Presigner s3Presigner;

  @Value("${aws.s3.bucket}")
  private String bucket;
  private final static String FILE_PREFIX = "file";
  private final static String TEMPORARY_FILE_PREFIX = "temp";


  @Override
  public PreSignedUrlDto generatePreSignedUrl(String fileName, FileDomain fileDomain) {
    var fileKey = createFileKey(fileName, fileDomain.getDirectory(), fileDomain.isTemporary());
    var putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(fileKey).build();

    var preSignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))//10분 안에 업로드 해야함
        .putObjectRequest(putObjectRequest)
        .build();

    var preSignedRequest = s3Presigner.presignPutObject(preSignRequest);

    return PreSignedUrlDto.builder()
        .preSignedUrl(preSignedRequest.url().toExternalForm())
        .fileKey(fileKey)
        .build();
  }

  @Override
  public void deleteFile(String fileKey) {
    s3Client.deleteObject(builder -> builder.bucket(bucket).key(fileKey));
  }

  private static String createFileKey(String fileName, String directory, boolean isTemporary) {
    return (isTemporary ? TEMPORARY_FILE_PREFIX : FILE_PREFIX) + directory + "/" + UUID.randomUUID()
        + fileName;
  }
}
