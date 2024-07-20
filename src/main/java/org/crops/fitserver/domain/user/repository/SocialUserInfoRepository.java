package org.crops.fitserver.domain.user.repository;

import java.util.Optional;
import org.crops.fitserver.domain.user.domain.SocialUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SocialUserInfoRepository extends
    JpaRepository<SocialUserInfo, Long> {

  @Query("select s from SocialUserInfo s "
      + "left join fetch s.user u "
      + "where s.socialCode = :socialCode")
  Optional<SocialUserInfo> findBySocialCode(String socialCode);

  void deleteByUserId(Long userId);
}
