package org.crops.fitserver.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.crops.fitserver.global")
public class FeignConfig {

}

