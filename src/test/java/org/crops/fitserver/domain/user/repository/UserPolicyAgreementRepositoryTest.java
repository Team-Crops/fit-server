package org.crops.fitserver.domain.user.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.crops.fitserver.config.QueryDslTestConfig;
import org.crops.fitserver.domain.user.constant.PolicyType;
import org.crops.fitserver.domain.user.domain.User;
import org.crops.fitserver.domain.user.domain.UserPolicyAgreement;
import org.crops.fitserver.domain.user.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@DataJpaTest
@EnableJpaAuditing
@Import(QueryDslTestConfig.class)
@ActiveProfiles("test")
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserPolicyAgreementRepositoryTest {

  private final UserPolicyAgreementRepository userPolicyAgreementRepository;
  private final TestEntityManager em;

  @Test
  public void 같은정책_같은버전을_insert할_수_없다() {
    //given
    var user = User.builder()
        .userRole(UserRole.MEMBER)
        .build();
    user = em.persist(user);
    var userPolicyAgreement1 = UserPolicyAgreement.builder()
        .policyType(PolicyType.PRIVACY_POLICY)
        .isAgree(true)
        .user(user)

        .version("20240331")
        .build();
    var userPolicyAgreement2 = UserPolicyAgreement.builder()
        .policyType(PolicyType.PRIVACY_POLICY)
        .isAgree(true)
        .user(user)
        .version("20240331")
        .build();
    userPolicyAgreement1 = userPolicyAgreementRepository.save(userPolicyAgreement1);
    em.flush();
    //when
    ThrowingCallable callable = () -> {
      userPolicyAgreementRepository.save(userPolicyAgreement2);
      em.flush();
    };

    //then
    assertThatThrownBy(callable)
        .isInstanceOf(DataIntegrityViolationException.class);
  }
  @Test
  public void 같은정책_다른버전은_insert할_수_있다() {
    //given
    var user = User.builder()
        .userRole(UserRole.MEMBER)
        .build();
    user = em.persist(user);
    var userPolicyAgreement1 = UserPolicyAgreement.builder()
        .policyType(PolicyType.PRIVACY_POLICY)
        .isAgree(true)
        .user(user)

        .version("20240331")
        .build();
    var userPolicyAgreement2 = UserPolicyAgreement.builder()
        .policyType(PolicyType.PRIVACY_POLICY)
        .isAgree(true)
        .user(user)
        .version("20240332")
        .build();
    userPolicyAgreement1 = userPolicyAgreementRepository.save(userPolicyAgreement1);
    em.flush();
    //when

    userPolicyAgreementRepository.save(userPolicyAgreement2);
    em.flush();

    //then
    assertThat(userPolicyAgreementRepository.findAllByUserId(user.getId()).size()).isEqualTo(2);
  }


}
