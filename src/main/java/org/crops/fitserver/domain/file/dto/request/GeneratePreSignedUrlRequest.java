package org.crops.fitserver.domain.file.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.crops.fitserver.domain.file.constant.FileDomain;


public record GeneratePreSignedUrlRequest(
    @NotNull String fileName,
    @NotNull FileDomain fileDomain
) {

}
