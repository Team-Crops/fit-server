package org.crops.fitserver.domain.user.domain;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Users {

  private List<User> users;

  public static Users of(List<User> users) {
    return new Users(users);
  }
}
