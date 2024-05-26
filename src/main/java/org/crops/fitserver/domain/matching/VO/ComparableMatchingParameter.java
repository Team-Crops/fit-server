package org.crops.fitserver.domain.matching.VO;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.ToDoubleFunction;
import lombok.Builder;
import org.crops.fitserver.domain.matching.entity.Matching;

@Builder
public record ComparableMatchingParameter(
    Double avgActivityHour,
    Double avgProjectCount,
    Double avgSkillCount,
    Double maxActivityHour,
    Double maxProjectCount,
    Double maxSkillCount,
    Double minActivityHour,
    Double minProjectCount,
    Double minSkillCount
) {

  public static ComparableMatchingParameter from(Matching matching) {
    var activityHour = Double.valueOf(matching.getUser().getUserInfo().getActivityHour());
    var projectCount = Double.valueOf(matching.getUser().getUserInfo().getProjectCount());
    var skillCount = (double) matching.getUser().getUserInfo().getUserInfoSkills().size();

    return ComparableMatchingParameter.builder()
        .avgActivityHour(activityHour)
        .avgProjectCount(projectCount)
        .avgSkillCount(skillCount)
        .maxActivityHour(activityHour)
        .maxProjectCount(projectCount)
        .maxSkillCount(skillCount)
        .minActivityHour(activityHour)
        .minProjectCount(projectCount)
        .minSkillCount(skillCount)
        .build();
  }

  public static ComparableMatchingParameter from(List<Matching> matchingList) {
    var activityHourStats = calculateStatistics(matchingList,
        matching -> Double.valueOf(matching.getUser().getUserInfo().getActivityHour()));
    var projectCountStats = calculateStatistics(matchingList,
        matching -> Double.valueOf(matching.getUser().getUserInfo().getProjectCount()));
    var skillCountStats = calculateStatistics(matchingList,
        matching -> matching.getUser().getUserInfo().getUserInfoSkills().size());

    return ComparableMatchingParameter.builder()
        .avgActivityHour(activityHourStats.getAverage())
        .avgProjectCount(projectCountStats.getAverage())
        .avgSkillCount(skillCountStats.getAverage())
        .maxActivityHour(activityHourStats.getMax())
        .maxProjectCount(projectCountStats.getMax())
        .maxSkillCount(skillCountStats.getMax())
        .minActivityHour(activityHourStats.getMin())
        .minProjectCount(projectCountStats.getMin())
        .minSkillCount(skillCountStats.getMin())
        .build();
  }

  public static Boolean isSimilar(ComparableMatchingParameter a, ComparableMatchingParameter b) {
    return Math.abs(a.avgActivityHour() - b.avgActivityHour()) <= 3
        && Math.abs(a.avgProjectCount() - b.avgProjectCount()) <= 2;
  }

  public static Double calculateSimilarity(ComparableMatchingParameter a,
      ComparableMatchingParameter b) {
    return Math.abs(a.avgActivityHour() - b.avgActivityHour()) * 10
        + Math.abs(a.avgProjectCount() - b.avgProjectCount()) * 100p
        + Math.abs(a.avgSkillCount() - b.avgSkillCount());
  }

  private static DoubleSummaryStatistics calculateStatistics(List<Matching> matchingList,
      ToDoubleFunction<Matching> mapper) {
    return matchingList.stream().mapToDouble(mapper).summaryStatistics();
  }

}
