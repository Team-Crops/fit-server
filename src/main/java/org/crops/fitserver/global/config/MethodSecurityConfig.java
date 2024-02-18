package org.crops.fitserver.global.config;

import org.springframework.aop.Advisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Role;
import org.springframework.security.authorization.method.AuthorizationManagerAfterMethodInterceptor;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = false)
public class MethodSecurityConfig {


  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  @Profile("prod")
  Advisor preAuthorize() {
    return AuthorizationManagerBeforeMethodInterceptor.preAuthorize();
  }

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  @Profile("prod")
  Advisor postAuthorize() {
    return AuthorizationManagerAfterMethodInterceptor.postAuthorize();
  }
}
