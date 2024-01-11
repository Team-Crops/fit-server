package org.crops.fitserver.global.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.global.annotation.SocketController;
import org.crops.fitserver.global.annotation.SocketMapping;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAddMappingSupporter {

  private final ConfigurableListableBeanFactory beanFactory;
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
    for (Method method : methods) {
      SocketMapping socketMapping = method.getAnnotation(SocketMapping.class);
      String endpoint = socketMapping.endpoint();
      Class<?> dtoClass = socketMapping.requestCls();

      socketIOServer.addEventListener(endpoint, dtoClass, ((client, data, ackSender) -> {
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
      }));
    }
  }

  private List<Method> findSocketMappingAnnotatedMethods(Class<?> cls) {
    return Arrays.stream(cls.getMethods())
        .filter(method -> method.getAnnotation(SocketMapping.class) != null)
        .collect(Collectors.toList());
  }
}