package org.crops.fitserver.domain.project.util;

import org.crops.fitserver.domain.matching.entity.MatchingRoom;
import org.crops.fitserver.domain.project.entity.Project;
import org.crops.fitserver.domain.project.entity.ProjectMember;

public class ProjectConverter {

  public static Project convertMatchingRoomToProject(MatchingRoom matchingRoom) {
    var newProject = Project.create(matchingRoom.getChatRoomId());
    matchingRoom.getMatchingList()
        .forEach(matching ->
            {
              var projectMember = ProjectMember.create(matching.getUser(), matching.getPosition());
              newProject.addMember(projectMember);
            }
        );
    return newProject;
  }

}
