package org.crops.fitserver.global.log;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {
    @Bean
    public FilterRegistrationBean<LogFilter> firstFilter(){
        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LogFilter());
        registrationBean.addUrlPatterns("*");
        registrationBean.setOrder(1);
        registrationBean.setName("LogFilter");
        return registrationBean;
    }
}
