package org.crops.fitserver.config;

import static org.crops.fitserver.global.util.JsonUtil.toJson;

import java.util.List;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.user.constant.LinkType;
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
import org.crops.fitserver.domain.user.domain.Link;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.User.UserBuilder;
import org.crops.fitserver.domain.user.domain.UserInfo;
import org.crops.fitserver.domain.user.domain.UserRole;

public class UserBuildUtil {

  public static UserBuilder buildNewUser() {
    var linkList = List.of(
        Link.builder().linkType(LinkType.GITHUB).linkUrl("github.com").build(),
        Link.builder().linkType(LinkType.ETC).linkUrl("test.com").build()
    );
    var userInfo = UserInfo.builder()
        .portfolioUrl("test.com")
        .projectCount(1)
        .activityHour((short) 1)
        .introduce("test")
        .isOpenProfile(true)
        .linkJson(toJson(linkList))
        .status(UserInfoStatus.COMPLETE)
        .build();
    return User.builder()
        .userRole(UserRole.MEMBER)
        .profileImageUrl("test.com")
        .phoneNumber("010-1234-1234")
        .isOpenPhoneNum(true)
        .email("test@gmail.com")
        .userInfo(userInfo)
        ;
  }

  public static UserBuilder buildUser() {
    return buildUser(1L);
  }

  private static UserBuilder buildUser(Long userId){
    var linkList = List.of(
        Link.builder().linkType(LinkType.GITHUB).linkUrl("github.com").build(),
        Link.builder().linkType(LinkType.ETC).linkUrl("test.com").build()
    );
    var userInfo = UserInfo.builder()
        .id(userId)
        .portfolioUrl("test.com")
        .projectCount(1)
        .activityHour((short) 1)
        .introduce("test")
        .isOpenProfile(true)
        .position(Position.builder().id(1L).build())
        .region(Region.builder().id(1L).build())
        .linkJson(toJson(linkList))
        .status(UserInfoStatus.COMPLETE)
        .build();
    return User.builder()
        .id(userId)
        .userRole(UserRole.MEMBER)
        .profileImageUrl("test.com")
        .username("test")
        .nickname("test")
        .phoneNumber("010-1234-1234")
        .isOpenPhoneNum(true)
        .email("test@gmail.com")
        .userInfo(userInfo)
        ;
  }
}
