package org.crops.fitserver.global.socket.security;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketDisConnectController implements DisconnectListener {

  @Override
  @Transactional
  public void onDisconnect(SocketIOClient socketIOClient) {
    log.info("Client[{}] - Disconnected from socket", socketIOClient.getSessionId().toString());
//    userRepository.findById(client.get("userInfo"))
//        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
  }
}
