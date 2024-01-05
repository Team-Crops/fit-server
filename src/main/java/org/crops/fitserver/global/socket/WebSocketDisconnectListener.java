package org.crops.fitserver.global.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketDisconnectListener implements DisconnectListener {

  @Override
  @Transactional
  public void onDisconnect(SocketIOClient socketIOClient) {
    log.info("Socket ID[{}] - Disconnected from socket", socketIOClient.getSessionId().toString());
  }
}
