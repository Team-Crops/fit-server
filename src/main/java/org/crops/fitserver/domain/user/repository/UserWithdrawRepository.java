package org.crops.fitserver.domain.user.repository;

import org.crops.fitserver.domain.user.domain.UserWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWithdrawRepository extends JpaRepository<UserWithdraw, Long> {
}
