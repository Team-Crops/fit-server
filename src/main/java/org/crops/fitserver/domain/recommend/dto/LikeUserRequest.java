package org.crops.fitserver.domain.recommend.dto;

public record LikeUserRequest(
    Long userId,
    Boolean like
) {

}
