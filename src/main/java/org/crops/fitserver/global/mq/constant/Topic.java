package org.crops.fitserver.global.mq.constant;

import lombok.Getter;
import org.crops.fitserver.global.mq.dto.Message;
import org.crops.fitserver.global.mq.dto.Report;
import org.crops.fitserver.global.socket.service.MessageResponse;
import org.springframework.data.redis.listener.ChannelTopic;

@Getter
public enum Topic {
  REPORT("report", Report.class),
  CHAT("chat", MessageResponse.class),
  ;

  private final String topic;
  private final Class<? extends Message> clazz;
  private final ChannelTopic channelTopic;

  Topic(String topic, Class<? extends Message> clazz) {
    this.topic = topic;
    this.clazz = clazz;
    this.channelTopic = new ChannelTopic(topic);
  }
}
