package org.crops.fitserver.domain.user.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import org.crops.fitserver.domain.user.domain.Link;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.util.CollectionUtils;

public class LinkUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static List<Link> parseToLinkList(String linkJson) {
    if (StringUtils.isEmpty(linkJson)) {
      return List.of();
    }

    try {
      return objectMapper.readValue(linkJson, new TypeReference<List<Link>>() {
      });
    } catch (JsonProcessingException e) {
      throw new BusinessException(ErrorCode.PARSE_JSON_EXCEPTION);
    }
  }

  public static String parseToJson(List<Link> linkList) {
    if (CollectionUtils.isEmpty(linkList)) {
      return "";
    }

    try {
      return objectMapper.writeValueAsString(linkList);
    } catch (JsonProcessingException e) {
      throw new BusinessException(ErrorCode.PARSE_JSON_EXCEPTION);
    }
  }
}
