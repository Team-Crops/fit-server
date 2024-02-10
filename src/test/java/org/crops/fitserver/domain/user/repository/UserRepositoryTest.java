package org.crops.fitserver.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.region.repository.RegionRepository;
import org.crops.fitserver.domain.skillset.domain.Position;
import org.crops.fitserver.domain.skillset.repository.PositionRepository;
import org.crops.fitserver.domain.user.constant.LinkType;
import org.crops.fitserver.domain.user.constant.UserInfoStatus;
import org.crops.fitserver.domain.user.domain.Link;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserInfo;
import org.crops.fitserver.domain.user.domain.UserRole;
import org.crops.fitserver.domain.user.dto.request.UpdateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class UserRepositoryTest {

  private final TestEntityManager em;

  private final UserRepository userRepository;

  private final PositionRepository positionRepository;

  private final RegionRepository regionRepository;

  private User user;
  private Position position;
  private Region region;
  private Region region2;

  @BeforeEach
  public void setUp() {
    userRepository.deleteAll();
    User user = User.builder()
        .userRole(UserRole.MEMBER)
        .profileImageUrl("test.jpg")
        .username("test")
        .nickname("test")
        .phoneNumber("010-1234-5678")
        .isOpenPhoneNum(false)
        .email("test@gmail.com")
        .userInfo(UserInfo.builder().build())
        .build();
    Position position = Position.builder()
        .displayName("test")
        .build();
    Region region = Region.builder()
        .displayName("test")
        .build();
    Region region2 = Region.builder()
        .displayName("test2")
        .build();
    this.position = positionRepository.save(position);
    this.region = regionRepository.save(region);
    this.region2 = regionRepository.save(region2);

    user.getUserInfo()
        .withPortfolioUrl("test.com")
        .withProjectCount(1)
        .withActivityHour(1)
        .withIntroduce("test")
        .withLinkJson("test")
        .withIsOpenProfile(false)
        .withPosition(position)
        .withRegion(region);
    this.user = userRepository.save(user);
    em.flush();
    em.clear();
  }

  @Test
  @Transactional
  public void update_user_with_info_failed_null_field() {
    //given
    User user = userRepository.findWithInfo(this.user.getId()).get();

    UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
        .profileImageUrl("test2.jpg")
        .username("test2")
        .nickname(null)
        .phoneNumber("010-1234-5678")
        .isOpenPhoneNum(false)
        .email("test@gmail.com")
        .portfolioUrl("test2.com")
        .projectCount(2)
        .activityHour(2)
        .introduce("test2")
        .linkList(List.of(
            Link.builder()
                .linkType(LinkType.GITHUB)
                .linkUrl("test2.com")
                .build()
        ))
        .isOpenProfile(false)
        .positionId(this.position.getId())
        .regionId(this.region2.getId())
        .build();

    //when

    ThrowingCallable result = () -> user.withNickname(updateUserRequest.getNickname());
    //then
    assertThatThrownBy(result).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @Transactional
  public void update_user_with_info() {
    //given
    User user = userRepository.findById(this.user.getId()).get();

    UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
        .profileImageUrl("test2.jpg")
        .username("test2")
        .nickname("test2")
        .phoneNumber("010-1234-5678")
        .isOpenPhoneNum(false)
        .email("test@gmail.com")
        .portfolioUrl("test2.com")
        .projectCount(2)
        .activityHour(2)
        .introduce("test2")
        .linkList(List.of(
            Link.builder()
                .linkType(LinkType.GITHUB)
                .linkUrl("test2.com")
                .build()
        ))
        .isOpenProfile(false)
        .positionId(this.position.getId())
        .regionId(this.region2.getId())
        .build();

    //when
    user.withEmail(updateUserRequest.getEmail())
        .withProfileImageUrl(updateUserRequest.getProfileImageUrl())
        .withUsername(updateUserRequest.getUsername())
        .withNickname(updateUserRequest.getNickname())
        .withPhoneNumber(updateUserRequest.getPhoneNumber())
        .withIsOpenPhoneNum(updateUserRequest.getIsOpenPhoneNum());

    user.getUserInfo()
        .withBackground(updateUserRequest.getBackgroundStatus(),
            updateUserRequest.getBackgroundText())
        .withPortfolioUrl(updateUserRequest.getPortfolioUrl())
        .withProjectCount(updateUserRequest.getProjectCount())
        .withActivityHour(updateUserRequest.getActivityHour())
        .withIntroduce(updateUserRequest.getIntroduce())
        .withLinkJson(Link.parseToJson(updateUserRequest.getLinkList()))
        .withIsOpenProfile(updateUserRequest.getIsOpenProfile())
        .withPosition(positionRepository.findById(updateUserRequest.getPositionId()).get())
        .withRegion(regionRepository.findById(updateUserRequest.getRegionId()).get());
    userRepository.save(user);
    em.flush();
    em.clear();

    //then
    User result = userRepository.findById(this.user.getId()).get();
    assertThat(result.getUsername()).isEqualTo("test2");
    assertThat(result.getUserInfo().getRegion().getId()).isEqualTo(this.region2.getId());
  }

}
