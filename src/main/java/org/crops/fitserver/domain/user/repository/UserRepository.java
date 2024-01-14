package org.crops.fitserver.domain.user.repository;

import org.crops.fitserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
