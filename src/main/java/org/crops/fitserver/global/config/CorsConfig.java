package org.crops.fitserver.global.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  @Value("${spring.profiles.active}")
  private String activeProfile;

  private static final List<String> PRODUCTION_ALLOWED_ORIGINS = List.of(
      "https://api.f-it.team"
  );

  private static final List<String> DEFAULT_ALLOWED_ORIGINS = List.of(
      "http://dev-api.f-it.team",
      "http://localhost:3000"
  );

  @Bean
  protected CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", getDefaultCorsConfiguration());
    return source;
  }

  private CorsConfiguration getDefaultCorsConfiguration() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(
        "prod".equals(activeProfile) ?
            PRODUCTION_ALLOWED_ORIGINS :
            DEFAULT_ALLOWED_ORIGINS);

    configuration.setAllowedMethods(
        Arrays.asList(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.DELETE.name()));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setExposedHeaders(Arrays.asList("Refresh-Token"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    return configuration;
  }
}