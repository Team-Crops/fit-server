package org.crops.fitserver.global.feign.oauth.google;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("oauth2.client.google")
public class GoogleClientProperty {

	private String contentType;
	private String grantType;
	private String clientId;
	private String clientSecret;
	private String loginPageUrl;
}
