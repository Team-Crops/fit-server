package org.crops.fitserver.user.repository;

import java.util.Optional;
import org.crops.fitserver.user.domain.SocialUserInfo;
import org.springframework.data.repository.Repository;

public interface SocialUserInfoRepository extends
		Repository<SocialUserInfo, Long> {

	Optional<SocialUserInfo> findBySocialCode(String socialCode);

	SocialUserInfo save(SocialUserInfo socialUserInfo);
}
