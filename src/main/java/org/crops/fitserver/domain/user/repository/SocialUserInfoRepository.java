package org.crops.fitserver.domain.user.repository;

import java.util.Optional;
import org.crops.fitserver.domain.user.domain.SocialUserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface SocialUserInfoRepository extends
    Repository<SocialUserInfo, Long> {

  @Query("select s from SocialUserInfo s "
      + "left join fetch s.user u "
      + "where s.socialCode = :socialCode")
  Optional<SocialUserInfo> findBySocialCode(String socialCode);

  SocialUserInfo save(SocialUserInfo socialUserInfo);
}
