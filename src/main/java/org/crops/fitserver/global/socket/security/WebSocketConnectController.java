package org.crops.fitserver.global.socket.security;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketConnectController implements ConnectListener {

  @Override
  @Transactional
  public void onConnect(SocketIOClient socketIOClient) {
    String room = socketIOClient.getHandshakeData().getSingleUrlParam("room");
    // room 조회 및 참가
    socketIOClient.joinRoom(room);
    log.info("roomName = {}", room);
    log.info("Socket ID[{}]  Connected to socket", socketIOClient.getSessionId().toString());
//    client.set("userInfo", jwtResolver.getUserIdFromAccessToken(token));
//    userRepository.findById(client.get("userInfo"))
//        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));

  }
}
