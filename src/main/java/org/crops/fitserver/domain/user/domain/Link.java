package org.crops.fitserver.domain.user.domain;

import static org.crops.fitserver.global.util.JsonUtil.parseJson;
import static org.crops.fitserver.global.util.JsonUtil.toJson;

import com.fasterxml.jackson.core.type.TypeReference;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import lombok.Builder;
import org.crops.fitserver.domain.user.constant.LinkType;
import org.springframework.util.CollectionUtils;

@Builder
public record Link(String linkUrl, LinkType linkType) {

  public static List<Link> parseToLinkList(String linkJson) {
    if (StringUtils.isBlank(linkJson)) {
      return List.of();
    }

    return parseJson(linkJson, new TypeReference<>() {
    });
  }

  public static String parseToJson(List<Link> linkList) {
    if (CollectionUtils.isEmpty(linkList)) {
      return null;
    }

    return toJson(linkList);
  }
}
