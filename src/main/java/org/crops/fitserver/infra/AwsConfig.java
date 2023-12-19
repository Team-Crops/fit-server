package org.crops.fitserver.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfig {

  @Value("${aws.accessKey}")
  private String accessKey;

  @Value("${aws.secretKey}")
  private String secretKey;

  @Value("${aws.region}")
  private String region;

  private AwsCredentials getCredentials() {
    return AwsBasicCredentials.create(accessKey, secretKey);
  }

  @Bean
  public S3Client S3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(this::getCredentials)
        .forcePathStyle(true)
        .build();
  }

  @Bean
  public S3Presigner S3Presigner() {
    return S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(this::getCredentials)
        .build();
  }

}
