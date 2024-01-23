package org.crops.fitserver.global.config;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.user.domain.UserRole;
import org.crops.fitserver.global.filter.JwtAccessDeniedHandler;
import org.crops.fitserver.global.filter.JwtAuthenticationFilter;
import org.crops.fitserver.global.filter.JwtExceptionFilter;
import org.crops.fitserver.global.http.HeaderTokenExtractor;
import org.crops.fitserver.global.jwt.JwtResolver;
import org.crops.fitserver.global.security.PrincipalDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtExceptionFilter jwtExceptionFilter;
  private final JwtAccessDeniedHandler customAccessDeniedHandler;
  private final HeaderTokenExtractor headerTokenExtractor;
  private final PrincipalDetailsService principalDetailsService;
  private final JwtResolver jwtResolver;

  private static final String[] apiDocumentPatterns = {
      "/swagger-resources/**",
      "/swagger-ui.html",
      "/swagger-ui/**",
      "/v3/api-docs/**",
      "/api-docs/**",
      "/webjars/**",
      "/docs/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    JwtAuthenticationFilter jwtAuthenticationFilter =
        new JwtAuthenticationFilter(
            principalDetailsService,
            headerTokenExtractor,
            jwtResolver);
    http
        .addFilterBefore(jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtExceptionFilter,
            JwtAuthenticationFilter.class)
        .exceptionHandling(
            exceptionHandling -> exceptionHandling
                .accessDeniedHandler(customAccessDeniedHandler)
        )
        .sessionManagement(
            sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .httpBasic(
            AbstractHttpConfigurer::disable
        )
        .csrf(
            AbstractHttpConfigurer::disable
        )
        .cors(
            cors -> cors
                .configurationSource(corsConfigurationSource())
        )
    ;

    http.authorizeHttpRequests(
        authorize -> authorize
            .requestMatchers(new AntPathRequestMatcher("/admin/**"))
            .hasAuthority(UserRole.ADMIN.name())
            .requestMatchers(new AntPathRequestMatcher("/manager/**"))
            .hasAnyAuthority(
                UserRole.MANAGER.name(),
                UserRole.ADMIN.name())
            .anyRequest()
            .hasAnyAuthority(
                UserRole.MEMBER.name(),
                UserRole.MANAGER.name(),
                UserRole.ADMIN.name())
    );

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web
        .ignoring()
        .requestMatchers(
            Arrays.stream(apiDocumentPatterns)
                .map(AntPathRequestMatcher::new)
                .toArray(AntPathRequestMatcher[]::new))
        .requestMatchers(new AntPathRequestMatcher("/actuator/**"))
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        .requestMatchers(new AntPathRequestMatcher("/v1/auth/social/**"))
        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")
        );
  }

  @Bean
  protected CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", getDefaultCorsConfiguration());
    return source;
  }

  private CorsConfiguration getDefaultCorsConfiguration() {
    CorsConfiguration configuration = new CorsConfiguration();

    //active profile에 따라 다르게 설정
    // prod를 제외한 나머지는 모두 localhost:3000을 허용

    var activeProfile = System.getProperty("spring.profiles.active");

    if("prod".equals(activeProfile)) {
      configuration.setAllowedOrigins(List.of("https://api.f-it.com"));
    } else {
      configuration.setAllowedOrigins(List.of("https://api.f-it.com", "http://localhost:3000"));
    }

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
