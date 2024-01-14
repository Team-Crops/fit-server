package org.crops.fitserver.global.annotation;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.crops.fitserver.global.socket.validator.ChatMessageTypeValidator;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChatMessageTypeValidator.class)
public @interface ChatMessageType {

  String message() default "잘못된 채팅 메세지 타입입니다. (TEXT, IMAGE 중 하나여야 합니다.)";

  Class[] groups() default {};

  Class[] payload() default {};
}
