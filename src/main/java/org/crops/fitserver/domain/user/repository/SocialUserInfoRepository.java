package org.crops.fitserver.domain.user.repository;

import java.util.Optional;
import org.crops.fitserver.domain.user.domain.SocialUserInfo;
import org.springframework.data.repository.Repository;

public interface SocialUserInfoRepository extends
    Repository<SocialUserInfo, Long> {

  Optional<SocialUserInfo> findBySocialCode(String socialCode);

  SocialUserInfo save(SocialUserInfo socialUserInfo);
}
