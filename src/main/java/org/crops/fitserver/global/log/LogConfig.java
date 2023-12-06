package org.crops.fitserver.global.log;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {

  @Bean
  public FilterRegistrationBean<LogFilter> LogFilter() {
    FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new LogFilter());
    registrationBean.addUrlPatterns("*");
    registrationBean.setOrder(1);
    registrationBean.setName("LogFilter");
    return registrationBean;
  }


  @Bean
  public FilterRegistrationBean<LogRequestFilter> LogRequestFilter() {
    FilterRegistrationBean<LogRequestFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new LogRequestFilter());
    registrationBean.addUrlPatterns("*");
    registrationBean.setOrder(2);
    registrationBean.setName("LogRequestFilter");
    return registrationBean;
  }
}
