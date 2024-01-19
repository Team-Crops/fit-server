package org.crops.fitserver.domain.user.domain;

import lombok.Builder;
import org.crops.fitserver.domain.user.constant.LinkType;

@Builder
public record Link(String linkUrl, LinkType linkType) {

}
