package org.crops.fitserver.user.repository;

import java.util.Optional;
import org.crops.fitserver.user.domain.User;
import org.springframework.data.repository.Repository;

public interface UserRepository extends
    Repository<User, Long>,
    UserRepositoryCustom {

  Optional<User> findById(Long id);

  User save(User user);
}
