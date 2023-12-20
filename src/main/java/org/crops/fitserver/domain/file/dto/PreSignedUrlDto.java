package org.crops.fitserver.domain.file.dto;

import lombok.Builder;

@Builder
public record PreSignedUrlDto(
    String preSignedUrl,
    String fileKey
) {

}
