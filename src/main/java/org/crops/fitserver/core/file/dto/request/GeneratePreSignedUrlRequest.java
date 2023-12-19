package org.crops.fitserver.core.file.dto.request;

import jakarta.validation.constraints.NotNull;
import org.crops.fitserver.core.file.constant.FileDomain;


public record GeneratePreSignedUrlRequest(
    @NotNull String fileName,
    @NotNull FileDomain fileDomain
) {

}
