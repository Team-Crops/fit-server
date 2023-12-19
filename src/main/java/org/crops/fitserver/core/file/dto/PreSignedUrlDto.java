package org.crops.fitserver.core.file.dto;

import lombok.Builder;

@Builder
public record PreSignedUrlDto(
    String preSignedUrl,
    String fileKey
) {

}
