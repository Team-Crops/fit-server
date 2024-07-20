package org.crops.fitserver.domain.recommend.dto;

public record LikeUserRequest(
    long userId,
    boolean like
) {

}
