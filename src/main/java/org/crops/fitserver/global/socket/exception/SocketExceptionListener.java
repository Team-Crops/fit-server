package org.crops.fitserver.global.socket.exception;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ExceptionListener;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.global.exception.ErrorResponse;
import org.springframework.stereotype.Component;

@Component
public class SocketExceptionListener implements ExceptionListener {

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
    return false;
  }

  private void runExceptionHandling(Exception e, SocketIOClient client) {
    final ErrorResponse message;
    if (e instanceof BusinessException) {
      BusinessException fitException = (BusinessException)e;
      message = ErrorResponse.from(fitException.getErrorCode());
    } else {
      e.printStackTrace();
      message = ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR);
    }
    client.sendEvent("error", message);
  }

}
