package org.crops.fitserver.global.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.domain.chat.service.ChatRoomService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomConnectListener implements ConnectListener {

  private final ChatRoomService chatRoomService;

  @Override
  @Transactional
  public void onConnect(SocketIOClient socketIOClient) {
    String roomId = socketIOClient.getHandshakeData().getSingleUrlParam("roomId");
    if (roomId != null) {
      log.info("Socket ID[{}]  Connected to socket", socketIOClient.getSessionId().toString());
      chatRoomService.getById(Long.parseLong(roomId));
      socketIOClient.joinRoom(roomId);
    }
  }
}
