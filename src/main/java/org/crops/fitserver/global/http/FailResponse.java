package org.crops.fitserver.global.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FailResponse {

  private int code;

  private String message;
}
