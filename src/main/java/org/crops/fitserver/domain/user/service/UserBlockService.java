package org.crops.fitserver.domain.user.service;

public interface UserBlockService {

    boolean canBlockUser(Long userId);

    void blockUser(Long userId);
}
