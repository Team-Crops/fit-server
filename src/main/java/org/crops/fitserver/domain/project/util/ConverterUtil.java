package org.crops.fitserver.domain.project.util;

import org.crops.fitserver.domain.matching.entity.MatchingRoom;
import org.crops.fitserver.domain.project.entity.Project;
import org.crops.fitserver.domain.project.entity.ProjectMember;

/**
 * 이 부분이 조금 가독성이 안좋다고 생각하는데 좋은 방법이 있을지,,,
 */
public class ConverterUtil {

  //matchingRoom to project
  public static Project convertMatchingRoomToProject(MatchingRoom matchingRoom) {
    var newProject = Project.create(matchingRoom.getChatRoomId());
    matchingRoom.getMatchingList()
        .forEach(matching ->
            ProjectMember.create(matching.getUser(), newProject, matching.getPosition())
        );
    return newProject;
  }

}
