package org.crops.fitserver.domain.user.repository;

import java.util.Optional;
import org.crops.fitserver.domain.user.domain.User;
import org.springframework.data.repository.Repository;

public interface UserRepository extends
    Repository<User, Long> {

  Optional<User> findById(Long id);

  User save(User user);
}
