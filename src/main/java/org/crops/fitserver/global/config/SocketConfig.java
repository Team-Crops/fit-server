package org.crops.fitserver.global.config;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.socket.SocketProperty;
import org.crops.fitserver.global.socket.security.WebSocketAddMappingSupporter;
import org.crops.fitserver.global.socket.domain.dto.MessageType;
import org.crops.fitserver.global.socket.exception.SocketExceptionListener;
import org.crops.fitserver.global.socket.security.WebSocketConnectController;
import org.crops.fitserver.global.socket.security.WebSocketDisConnectController;
import org.crops.fitserver.global.socket.domain.dto.Message;
import org.crops.fitserver.global.socket.domain.SocketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SocketConfig {

  private final WebSocketAddMappingSupporter mappingSupporter;
  private final WebSocketConnectController connectController;
  private final WebSocketDisConnectController disConnectController;
  private final SocketExceptionListener exceptionListener;
  private final SocketService socketService;
  private final SocketProperty socketProperty;

  @Bean
  public SocketIOServer socketIOServer() {
    com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
    config.setPort(socketProperty.getPort());
    config.setOrigin("*");
    SocketIOServer server = new SocketIOServer(config);
    mappingSupporter.addListeners(server);
    server.addConnectListener(connectController);
    server.addDisconnectListener(disConnectController);
    config.setExceptionListener(exceptionListener);
//    server.addEventListener("send_message", Message.class, onChatReceived());
    return server;
  }

  private DataListener<Message> onChatReceived() {
    return (senderClient, data, ackSender) -> {
      String room = senderClient.getHandshakeData().getSingleUrlParam("room");
      socketService.sendMessage(room, "get_message", senderClient, data.getContent());
      for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
        if (!client.getSessionId().equals(senderClient.getSessionId())) {
          client.sendEvent("get_message", new Message(MessageType.TEXT, data.getContent()));
        }
      }
    };
  }
}