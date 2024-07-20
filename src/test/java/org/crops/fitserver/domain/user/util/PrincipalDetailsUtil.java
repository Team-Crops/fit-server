package org.crops.fitserver.domain.user.util;

import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserRole;
import org.crops.fitserver.global.security.PrincipalDetails;

public class PrincipalDetailsUtil {

  public static PrincipalDetails getPrincipalDetails() {

    return getPrincipalDetails(1L);
  }

  public static PrincipalDetails getPrincipalDetails(Long id) {

    return getPrincipalDetails(id, UserRole.MEMBER);
  }

  public static PrincipalDetails getPrincipalDetails(Long id, UserRole role) {

    return PrincipalDetails.from(User.builder().id(id).userRole(role).build());
  }

}
