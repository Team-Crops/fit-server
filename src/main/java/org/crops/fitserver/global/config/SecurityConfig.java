package org.crops.fitserver.global.config;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.user.domain.UserRole;
import org.crops.fitserver.global.filter.JwtAccessDeniedHandler;
import org.crops.fitserver.global.filter.JwtAuthenticationFilter;
import org.crops.fitserver.global.filter.JwtExceptionFilter;
import org.crops.fitserver.global.http.HeaderTokenExtractor;
import org.crops.fitserver.global.jwt.JwtResolver;
import org.crops.fitserver.global.security.PrincipalDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtExceptionFilter jwtExceptionFilter;
  private final CorsConfigurationSource corsConfigurationSource;
  private final JwtAccessDeniedHandler customAccessDeniedHandler;
  private final HeaderTokenExtractor headerTokenExtractor;
  private final PrincipalDetailsService principalDetailsService;
  private final JwtResolver jwtResolver;

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
                .configurationSource(corsConfigurationSource)
        );

    http.authorizeHttpRequests(
        authorize -> authorize
            .requestMatchers(new AntPathRequestMatcher("/admin/**"))
            .hasAuthority(UserRole.ADMIN.name())
            .requestMatchers(new AntPathRequestMatcher("/manager/**"))
            .hasAnyAuthority(
                UserRole.MANAGER.name(),
                UserRole.ADMIN.name())
            .anyRequest()
            .permitAll()
    );
    return http.build();
  }
}
