package org.crops.fitserver.domain.user.repository;

import org.crops.fitserver.domain.user.domain.UserWithdraw;
import org.springframework.data.repository.Repository;

public interface UserWithdrawRepository extends Repository<UserWithdraw, Long> {

  UserWithdraw save(UserWithdraw userWithdraw);
}
