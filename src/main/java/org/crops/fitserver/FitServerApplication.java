package org.crops.fitserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FitServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitServerApplication.class, args);
	}

}
