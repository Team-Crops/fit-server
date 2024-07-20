package org.crops.fitserver.global.socket.exception;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ExceptionListener;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.global.exception.ErrorResponse;
import org.crops.fitserver.global.socket.SocketProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketExceptionListener implements ExceptionListener {

  private final SocketProperty socketProperty;

  @Override
  public void onEventException(Exception e, List<Object> args, SocketIOClient client) {
    runExceptionHandling(e, client);
  }

  @Override
  public void onDisconnectException(Exception e, SocketIOClient client) {
    runExceptionHandling(e, client);
  }

  @Override
  public void onConnectException(Exception e, SocketIOClient client) {
    runExceptionHandling(e, client);
    client.disconnect();
  }

  @Override
  public void onPingException(Exception e, SocketIOClient client) {
    runExceptionHandling(e, client);
  }

  @Override
  public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
    e.printStackTrace();
    return false;
  }

  private void runExceptionHandling(Exception e, SocketIOClient client) {
    if (e.getCause() instanceof BusinessException) {
      BusinessException businessException = (BusinessException) e.getCause();
      log.error("BusinessException : {}", businessException.getMessage(), e);
      client.sendEvent(
          socketProperty.getGetMessageEvent(),
          ErrorResponse.from(
              businessException.getErrorCode()));
    } else {
      log.error("Exception : {}", e.getMessage(), e);
      client.sendEvent(
          socketProperty.getGetMessageEvent(),
          ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }
  }
}