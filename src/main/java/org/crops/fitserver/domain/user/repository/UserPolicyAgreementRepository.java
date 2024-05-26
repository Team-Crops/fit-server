package org.crops.fitserver.domain.user.repository;

import java.util.List;
import org.crops.fitserver.domain.user.domain.UserPolicyAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserPolicyAgreementRepository extends JpaRepository<UserPolicyAgreement, Long> {

  @Query("select case when count(u) > 0 then true else false end from UserPolicyAgreement u where u.id = ?1")
  boolean existsById(Long id);

  List<UserPolicyAgreement> findAllByUserId(long userId);

  void deleteAllByUserId(long userId);
}
