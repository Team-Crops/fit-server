
package org.crops.fitserver.global.socket.service;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.chat.domain.MessageType;
import org.crops.fitserver.domain.chat.dto.response.ImageMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.NoticeMessageResponse;
import org.crops.fitserver.domain.chat.dto.response.TextMessageResponse;
import org.crops.fitserver.global.mq.dto.Message;
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@JsonTypeInfo(use = DEDUCTION)
@JsonSubTypes({
    @Type(value = NoticeMessageResponse.class),
    @Type(value = TextMessageResponse.class),
    @Type(value = ImageMessageResponse.class)
})
public abstract class SocketResponse implements Message {

  protected MessageType messageType;
}