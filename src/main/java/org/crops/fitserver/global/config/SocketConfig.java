package org.crops.fitserver.global.config;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.socket.SocketProperty;
import org.crops.fitserver.global.socket.WebSocketAddMappingSupporter;
import org.crops.fitserver.global.socket.exception.SocketExceptionListener;
import org.crops.fitserver.global.socket.ChatRoomConnectListener;
import org.crops.fitserver.global.socket.WebSocketDisconnectListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SocketConfig implements CommandLineRunner {

  private final WebSocketAddMappingSupporter mappingSupporter;
  private final ChatRoomConnectListener connectListener;
  private final WebSocketDisconnectListener disconnectListener;
  private final SocketExceptionListener exceptionListener;
  private final SocketProperty socketProperty;

  @Bean
  public SocketIOServer socketIOServer() {
    com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
    config.setPort(socketProperty.getPort());
    config.setOrigin("*");
    config.setExceptionListener(exceptionListener);
    SocketIOServer server = new SocketIOServer(config);
    mappingSupporter.addListeners(server);
    server.addConnectListener(connectListener);
    server.addDisconnectListener(disconnectListener);
    return server;
  }

  @Override
  public void run(String... args) throws Exception {
    socketIOServer().start();
  }
}