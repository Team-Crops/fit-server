package org.crops.fitserver.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.crops.fitserver.domain.region.repository.RegionRepository;
import org.crops.fitserver.domain.skillset.repository.PositionRepository;
import org.crops.fitserver.domain.skillset.repository.SkillRepository;
import org.crops.fitserver.domain.user.constant.LinkType;
import org.crops.fitserver.domain.user.constant.PolicyType;
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
import org.crops.fitserver.domain.user.domain.Link;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserInfo;
import org.crops.fitserver.domain.user.domain.UserPolicyAgreement;
import org.crops.fitserver.domain.user.domain.UserRole;
import org.crops.fitserver.domain.user.dto.PolicyAgreementDto;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.crops.fitserver.domain.user.repository.UserPolicyAgreementRepository;
import org.crops.fitserver.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  private final User.UserBuilder userBuilder = User.builder()
      .id(1L)
      .userRole(UserRole.MEMBER)
      .profileImageUrl("test.jpg")
      .username("test")
      .nickname("test")
      .phoneNumber("010-1234-5678")
      .isOpenPhoneNum(false)
      .email("test@gmail.com");
  private final UserInfo.UserInfoBuilder userInfoBuilder = UserInfo.builder()
      .id(1L)
      .user(userBuilder.build())
      .portfolioUrl("test.com")
      .projectCount(1)
      .activityHour((short) 1)
      .introduce("test")
      .linkJson("test")
      .status(UserInfoStatus.INCOMPLETE)
      .isOpenProfile(false);
  @InjectMocks
  private UserServiceImpl userService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private PositionRepository positionRepository;
  @Mock
  private RegionRepository regionRepository;
  @Mock
  private SkillRepository skillRepository;
  @Mock
  private UserPolicyAgreementRepository userPolicyAgreementRepository;

  @Test
  public void getUserWithInfo_success() {
    //given
    var userId = 1L;
    var userBuilder = User.builder().id(userId);
    var userInfo = UserInfo.builder().id(1L).user(userBuilder.build()).build();
    var user = userBuilder.userInfo(userInfo).build();

    given(userRepository.findWithInfo(userId)).willReturn(Optional.of(user));

    //when
    var result = userService.getUserWithInfo(userId);

    //then
    assertThat(result).isEqualTo(user);
  }

  @Test
  public void updateUserInfo_fail_fill_null_already_exists() {
    //given
    var userId = 1L;
    var updateUserRequest = UpdateUserRequest.builder()
        .profileImageUrl("test.jpg")
        .username("test")
        .nickname(null)
        .phoneNumber("010-1234-5678")
        .isOpenPhoneNum(false)
        .email("test@gmail.com")
        .portfolioUrl("test.com")
        .projectCount(1)
        .activityHour((short) 1)
        .introduce("test")
        .linkList(List.of(
            Link.builder()
                .linkType(LinkType.GITHUB)
                .linkUrl("test2.com")
                .build()
        ))
        .isOpenProfile(false)
        .build();
    var userInfo = userInfoBuilder.build();
    var user = userBuilder.userInfo(userInfo).build();
    given(userRepository.findById(userId)).willReturn(Optional.of(user));

    //when
    ThrowingCallable result = () -> userService.updateUserWithInfo(userId, updateUserRequest);

    //then
    assertThatThrownBy(result).isInstanceOf(IllegalArgumentException.class);

  }

  @Test
  public void getPolicyAgreementList_success() {
    //given
    var userId = 1L;
    var userPolicyAgreement = UserPolicyAgreement
        .builder()
        .policyType(PolicyType.PRIVACY_POLICY)
        .id(1L)
        .user(userBuilder.build())
        .isAgree(true)
        .build();
    given(userPolicyAgreementRepository.findAllByUserId(userId)).willReturn(
        List.of(userPolicyAgreement));

    //when
    var result = userService.getPolicyAgreementList(userId);

    //then
    assertThat(result.get(0).getUser().getId()).isEqualTo(userPolicyAgreement.getUser().getId());
  }

  @Test
  public void updatePolicyAgreement_중복() {
    //given
    var user = userBuilder.build();
    var policyType = PolicyType.PRIVACY_POLICY;
    var userPolicyAgreement = UserPolicyAgreement
        .builder()
        .policyType(policyType)
        .id(1L)
        .user(user)
        .version("1.0.0")
        .isAgree(false)
        .build();
    given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
    given(userPolicyAgreementRepository.findAllByUserId(user.getId())).willReturn(
        List.of(userPolicyAgreement));
    given(userPolicyAgreementRepository.saveAll(any())).willReturn(
        List.of(userPolicyAgreement));


    //when
    List<UserPolicyAgreement> result = userService.updatePolicyAgreement(user.getId(), List.of(
        PolicyAgreementDto.of(policyType, "1.0.0",true, null)));

    //then
    assertThat(result.get(0).getIsAgree()).isEqualTo(true);
  }
}
