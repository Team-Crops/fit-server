package org.crops.fitserver.domain.user.repository;

import static org.crops.fitserver.domain.user.domain.QUser.user;
import static org.crops.fitserver.domain.user.domain.QUserInfo.userInfo;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.user.constant.BackgroundStatus;
import org.crops.fitserver.domain.user.domain.User;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<User> findAllByFilter(
      Long userId,
      Boolean liked,
      List<Long> positionId,
      List<Long> skillId,
      BackgroundStatus backgroundStatus,
      Long regionId,
      Integer projectCount,
      List<Short> activityHour,
      int page,
      int size,
      int randomSeed) {
    return queryFactory
        .selectFrom(user)
        .leftJoin(user.userInfo, userInfo)
        .fetchJoin()
        .where(
            user.id.ne(userId),
            eqLiked(userId, liked),
            eqPositionId(positionId),
            eqSkillId(skillId),
            eqBackgroundStatus(backgroundStatus),
            eqRegionId(regionId),
            eqProjectCount(projectCount),
            eqActivityHour(activityHour)
        )
        .orderBy(
            Expressions.stringTemplate("RAND("+randomSeed+")").asc()
        )
        .offset(page * size)
        .limit(size)
        .fetch();
  }

  private BooleanExpression eqLiked(Long userId, Boolean liked) {
    return liked == null || !liked ? null : user.likedUsers.any().likeUser.id.eq(userId);
  }

  private BooleanExpression eqPositionId(List<Long> positionId) {
    return positionId == null || positionId.isEmpty() ? null : userInfo.position.id.in(positionId);
  }

  private static BooleanExpression eqSkillId(List<Long> skillId) {
    return skillId == null || skillId.isEmpty() ? null : userInfo.userInfoSkills.any().skill.id.in(skillId);
  }

  private static BooleanExpression eqBackgroundStatus(BackgroundStatus backgroundStatus) {
    return backgroundStatus == null ? null : userInfo.backgroundStatus.eq(backgroundStatus);
  }

  private static BooleanExpression eqRegionId(Long regionId) {
    return regionId == null ? null : userInfo.region.id.eq(regionId);
  }

  private static BooleanExpression eqProjectCount(Integer projectCount) {
    return projectCount == null ? null : userInfo.projectCount.eq(projectCount);
  }

  private static BooleanExpression eqActivityHour(List<Short> activityHour) {
    return activityHour == null || activityHour.isEmpty() ? null : userInfo.activityHour.in(activityHour);
  }
}
