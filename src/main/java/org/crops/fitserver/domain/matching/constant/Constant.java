package org.crops.fitserver.domain.matching.constant;

import java.util.Map;
import org.crops.fitserver.domain.skillset.constant.PositionType;

public class Constant {

  public static final int MATCHING_EXPIRE_DAYS = 3;

  public static final Map<PositionType, Integer> MINIMUM_REQUIRED_POSITIONS = Map.of(
      PositionType.PLANNER, 1,
      PositionType.DESIGNER, 1,
      PositionType.BACKEND, 1,
      PositionType.FRONTEND, 1
  );

  public static final Map<PositionType, Integer> MAXIMUM_POSITIONS = Map.of(
      PositionType.PLANNER, 2,
      PositionType.DESIGNER, 2,
      PositionType.BACKEND, 3,
      PositionType.FRONTEND, 4
  );


  public static final Map<PositionType, Integer> MULTIPLE_POSITION_COMPARE_TO_OTHER_POSITIONS = Map.of(
      PositionType.PLANNER, 1,
      PositionType.DESIGNER, 1,
      PositionType.BACKEND, 2,
      PositionType.FRONTEND, 2
  );


}
