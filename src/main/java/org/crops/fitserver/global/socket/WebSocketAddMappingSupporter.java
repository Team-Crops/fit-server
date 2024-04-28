package org.crops.fitserver.global.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.annotation.SocketController;
import org.crops.fitserver.global.annotation.SocketMapping;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.crops.fitserver.global.exception.ErrorResponse;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAddMappingSupporter {

  private final ConfigurableListableBeanFactory beanFactory;
  private final SocketProperty socketProperty;
  private SocketIOServer socketIOServer;

  public void addListeners(SocketIOServer socketIOServer) {
    this.socketIOServer = socketIOServer;
    final List<Class<?>> classes = beanFactory.getBeansWithAnnotation(SocketController.class)
        .values()
        .stream()
        .map(Object::getClass)
        .collect(Collectors.toList());

    classes.forEach(cls -> {
      List<Method> methods = findSocketMappingAnnotatedMethods(cls);
      addSocketServerEventListener(cls, methods);
    });
  }

  private void addSocketServerEventListener(Class<?> controller, List<Method> methods) {
    SocketController socketController = controller.getAnnotation(SocketController.class);
    String eventValue = socketController.value();
    methods.forEach(method -> {
      SocketMapping socketMapping = method.getAnnotation(SocketMapping.class);
      String endpoint = socketMapping.endpoint();
      Class<?> dtoClass = socketMapping.requestCls();
      StringBuilder sb = new StringBuilder();
      sb.append(eventValue);
      sb.append(endpoint);
      socketIOServer.addEventListener(sb.toString(), dtoClass, (client, data, ackSender) -> {
        try {
          List<Object> args = new ArrayList<>();
          for (Class<?> params : method.getParameterTypes()) {
            if (params.equals(SocketIOServer.class)) {
              args.add(socketIOServer);
            } else if (params.equals(SocketIOClient.class)) {
              args.add(client);
            } else if (params.equals(dtoClass)) {
              args.add(data);
            }
          }
          method.invoke(beanFactory.getBean(controller), args.toArray());
          } catch (Exception e) {
          exceptionHandle(e, client);
        }
      });
    });
  }

  private List<Method> findSocketMappingAnnotatedMethods(Class<?> cls) {
    return Arrays.stream(cls.getMethods())
        .filter(method -> method.getAnnotation(SocketMapping.class) != null)
        .collect(Collectors.toList());
  }

  private void exceptionHandle(Exception e, SocketIOClient client) {
    if (e.getCause() instanceof BusinessException) {
      BusinessException businessException = (BusinessException) e.getCause();
      log.error("BusinessException : {}", businessException.getMessage(), e);
      client.sendEvent(
          socketProperty.getGetMessageEvent(),
          ErrorResponse.from(businessException.getErrorCode()));
    } else {
      log.error("Exception : {}", e.getMessage(), e);
      client.sendEvent(
          socketProperty.getGetMessageEvent(),
          ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }
  }
}