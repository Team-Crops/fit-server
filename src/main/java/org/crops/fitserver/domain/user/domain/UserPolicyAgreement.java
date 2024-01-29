package org.crops.fitserver.domain.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crops.fitserver.domain.user.constant.PolicyType;
import org.crops.fitserver.global.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Table(name = "user_policy_agreement", indexes = {
    @Index(name = "user_policy_agreement_idx_1", columnList = "user_id, policy_type, version", unique = true),
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class UserPolicyAgreement extends BaseTimeEntity {

  @Id
  @GeneratedValue
  @Column(name = "user_policy_agreement_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false, name = "policy_type")
  @Enumerated(value = EnumType.STRING)
  private PolicyType policyType;

  @Column(nullable = false)
  private String version;

  @Column(nullable = false)
  private Boolean isAgree;

  public UserPolicyAgreement updateIsAgree(Boolean isAgree) {
    this.isAgree = isAgree;
    return this;
  }
}
